package com.unlikepaladin.pfm.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.RawLogTableBlock;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.*;

public class DynamicFurnitureRecipe implements FurnitureRecipe {
    private final String group;
    private final FurnitureOutput furnitureOutput;
    private final List<ResourceLocation> supportedVariants;
    private final FurnitureIngredients ingredients;
    public DynamicFurnitureRecipe(String group, FurnitureOutput furnitureOutput, List<ResourceLocation> supportedVariants, FurnitureIngredients furnitureIngredients) {
        this.group = group;
        this.furnitureOutput = furnitureOutput;
        this.supportedVariants = supportedVariants;
        this.ingredients = furnitureIngredients;
    }

    Map<ResourceLocation, List<FurnitureInnerRecipe>> furnitureInnerRecipes = Maps.newHashMap();
    public void constructInnerRecipes() {
        if (!furnitureInnerRecipes.isEmpty()) return;

        for (ResourceLocation id : supportedVariants) {
            VariantBase<?> variant = VariantHelper.getVariant(id);
            if (variant == null || furnitureInnerRecipes.containsKey(id)) continue;
            Optional<Block> optionalOutput;
            DataComponentPatch componentChanges = furnitureOutput.components != null ? furnitureOutput.components : DataComponentPatch.EMPTY;
            DataComponentMap.Builder builder = DataComponentMap.builder();

            if (!componentChanges.isEmpty() && componentChanges.entrySet().stream().anyMatch(dataComponentTypeOptionalEntry -> dataComponentTypeOptionalEntry.getKey() == PFMComponents.COLOR_COMPONENT)) {
                optionalOutput = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariantAndColor(variant, componentChanges.get(PFMComponents.COLOR_COMPONENT).get());
                if (optionalOutput.get().asItem().components().get(PFMComponents.COLOR_COMPONENT) == null) {
                    componentChanges = componentChanges.forget(dataComponentType -> dataComponentType == PFMComponents.COLOR_COMPONENT);
                    PatchedDataComponentMap.fromPatch(optionalOutput.get().asItem().components(), componentChanges);
                }
                else
                    builder.addAll(PatchedDataComponentMap.fromPatch(optionalOutput.get().asItem().components(), componentChanges));
            } else {
                optionalOutput = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariant(variant);
                builder.addAll(PatchedDataComponentMap.fromPatch(optionalOutput.get().asItem().components(), componentChanges));
            }
            if (optionalOutput.isEmpty()) continue;

            if (optionalOutput.get().asItem().components().get(PFMComponents.VARIANT_COMPONENT) != null) {
                builder.set(PFMComponents.VARIANT_COMPONENT, variant.identifier);
            }

            ItemStack output = new ItemStack(optionalOutput.get().asItem(), furnitureOutput.getOutputCount());
            DataComponentMap finalComponents = builder.build();
            if (!finalComponents.isEmpty())
                output.applyComponents(builder.build());

            Map<String, Integer> childrenToCountMap = ingredients.variantChildren;

            boolean abortVariant = false;
            List<Ingredient> stacks = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : childrenToCountMap.entrySet()) {
                ItemLike convertible = variant.getItemForRecipe(entry.getKey(), getOutputBlockClass());
                if (convertible == null || convertible.asItem() == Items.AIR){
                    abortVariant = true;
                    break;
                }
                for (int i = 0; i < entry.getValue(); i++)
                    stacks.add(Ingredient.of(convertible.asItem()));
            }

            // abort constructing for a variant if the recipe was invalid because of a missing ingredient, preferable over a crash
            if (abortVariant) {
                PaladinFurnitureMod.GENERAL_LOGGER.warn("Skipped constructing inner recipe for variant {} on recipe {}", variant.identifier, furnitureOutput.outputClass);
                continue;
            }

            List<FurnitureInnerRecipe> recipes = new ArrayList<>();

            FurnitureInnerRecipe recipe = new FurnitureInnerRecipe(this, output, stacks);
            recipes.add(recipe);


            if (variant instanceof WoodVariant woodVariant && woodVariant.hasStripped()) {
                List<Ingredient> strippedIngredients = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : childrenToCountMap.entrySet()) {
                    for (int i = 0; i < entry.getValue(); i++) {
                        if (getOutputBlockClass() == RawLogTableBlock.class)
                            strippedIngredients.add(Ingredient.of((Block)woodVariant.getChild("stripped_log")));
                        else
                            strippedIngredients.add(Ingredient.of(woodVariant.getItemForRecipe(entry.getKey(), getOutputBlockClass(), true).asItem()));
                    }
                }

                Optional<Block> strippedOptional = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariant(variant, true);
                if (strippedOptional.isPresent()) {

                    ItemStack strippedOutput = new ItemStack(strippedOptional.get(), furnitureOutput.getOutputCount());
                    if (!finalComponents.isEmpty())
                        strippedOutput.applyComponents(finalComponents);

                    FurnitureInnerRecipe stripped = new FurnitureInnerRecipe(this, strippedOutput, strippedIngredients);
                    recipes.add(stripped);
                }
            }
            furnitureInnerRecipes.put(id, recipes);
        }
    }

    @Override
    public boolean matches(FurnitureRecipe.FurnitureRecipeInput inventory, Level level) {
        constructInnerRecipes();
        for (ResourceLocation id : furnitureInnerRecipes.keySet()) {
            List<FurnitureInnerRecipe> recipes = furnitureInnerRecipes.get(id);
            for (FurnitureInnerRecipe recipe : recipes) {
                if (recipe.isInnerEnabled(level.enabledFeatures()) && recipe.matches(inventory, level))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<CraftableFurnitureRecipe> getAvailableOutputs(FurnitureRecipe.FurnitureRecipeInput input, HolderLookup.Provider registryManager) {
        constructInnerRecipes();
        Inventory inventory = input.playerInventory();
        List<CraftableFurnitureRecipe> stacks = Lists.newArrayList();
        for (ResourceLocation id : furnitureInnerRecipes.keySet()) {
            List<FurnitureInnerRecipe> recipes = furnitureInnerRecipes.get(id);
            for (FurnitureInnerRecipe recipe : recipes) {
                if (recipe.isInnerEnabled(inventory.player.level().enabledFeatures()) && recipe.matches(input, inventory.player.level()))
                    stacks.add(recipe);
            }
        }
        return stacks;
    }

    @Override
    public List<CraftableFurnitureRecipe> getInnerRecipes(FeatureFlagSet featureSet) {
        constructInnerRecipes();
        List<CraftableFurnitureRecipe> outputs = new ArrayList<>();
        for (List<FurnitureInnerRecipe> recipes : furnitureInnerRecipes.values())
            for (FurnitureInnerRecipe recipe : recipes)
                if (featureSet != null && recipe.isInnerEnabled(featureSet))
                    outputs.add(recipe);
                else if (featureSet == null)
                    outputs.add(recipe);
        return outputs;
    }

    @Override
    public String outputClass() {
        return furnitureOutput.outputClass;
    }

    @Override
    public ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager) {
        PaladinFurnitureMod.GENERAL_LOGGER.debug("Something has tried to craft a dynamic furniture recipe without context");
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResult(HolderLookup.Provider registryManager) {
        PaladinFurnitureMod.GENERAL_LOGGER.debug("Something has tried to get the output of a dynamic furniture recipe without context");
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends Recipe<FurnitureRecipeInput>> getSerializer() {
        return RecipeTypes.DYNAMIC_FURNITURE_SERIALIZER;
    }

    @Override
    public PlacementInfo placementInfo() {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (CraftableFurnitureRecipe recipe : getInnerRecipes(null)) {
            ingredientList.addAll(recipe.getIngredients());
        }
        return PlacementInfo.createFromOptionals(ingredientList.stream().map(Optional::of).toList());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    protected Class<? extends Block> getOutputBlockClass() {
        return FurnitureOutput.getOutputBlockClass(furnitureOutput.outputClass);
    }

    public List<ResourceLocation> getSupportedVariants() {
        return supportedVariants;
    }

    @Override
    public int getOutputCount(HolderLookup.Provider registryManager) {
        return furnitureOutput.getOutputCount();
    }

    @Override
    public int getMaxInnerRecipeSize() {
        return ingredients.vanillaIngredients.size()+ingredients.variantChildren.size();
    }

    @Override
    public List<? extends CraftableFurnitureRecipe> getInnerRecipesForVariant(Level level, ResourceLocation identifier){
        constructInnerRecipes();
        if (furnitureInnerRecipes.containsKey(identifier)) {
            return furnitureInnerRecipes.get(identifier);
        }
        return List.of();
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        Serializer.write(buf, this);
    }

    @Override
    public String getName(HolderLookup.Provider registryManager) {
        return outputClass().replaceAll("(?<=[a-z])(?=[A-Z])", " ");
    }

    private FurnitureOutput getOutput() {
        return this.furnitureOutput;
    }

    private FurnitureIngredients getInnerIngredients() {
        return this.ingredients;
    }

    @Override
    public List<Ingredient> getIngredients(Level world) {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (CraftableFurnitureRecipe recipe : getInnerRecipes(world.enabledFeatures())) {
            ingredientList.addAll(recipe.getIngredients());
        }
        return ingredientList;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof DynamicFurnitureRecipe that)) return false;
        return Objects.equals(group, that.group) && furnitureOutput.equals(that.furnitureOutput) && ingredients.equals(that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, furnitureOutput, ingredients);
    }

    Map<ItemStack, FurnitureInnerRecipe> outputToInnerRecipe = new HashMap<>();
    Map<Item, FurnitureInnerRecipe> outputItemToInnerRecipe = new HashMap<>();
    public static final class FurnitureInnerRecipe implements CraftableFurnitureRecipe {
        private final DynamicFurnitureRecipe parentRecipe;
        private final ItemStack output;
        private final List<Ingredient> ingredients;
        private final List<Ingredient> combinedIngredients;
        public FurnitureInnerRecipe(DynamicFurnitureRecipe parentRecipe, ItemStack output, List<Ingredient> ingredients) {
            this.parentRecipe = parentRecipe;
            this.output = output;
            this.ingredients = ingredients;
            this.combinedIngredients = Lists.newArrayList();
            this.combinedIngredients.addAll(ingredients);
            this.combinedIngredients.addAll(parentRecipe.ingredients.vanillaIngredients);
            parentRecipe.outputToInnerRecipe.put(output, this);
            parentRecipe.outputItemToInnerRecipe.put(output.getItem(), this);
        }

        @Override
        public ItemStack getResult(HolderLookup.Provider registryManager) {
            return output;
        }

        @Override
        public List<Ingredient> getIngredients() {
            return combinedIngredients;
        }

        @Override
        public boolean matches(FurnitureRecipe.FurnitureRecipeInput inventory, Level world) {
            Map<Item, Integer> ingredientCounts = getItemCounts();
            for (Map.Entry<Item, Integer> entry : ingredientCounts.entrySet()) {
                Item item = entry.getKey();
                Integer count = entry.getValue();

                int itemCount = 0;
                ItemStack defaultStack = item.getDefaultInstance();
                for (ItemStack stack1 : inventory.playerInventory().items) {
                    if (defaultStack.is(stack1.getItem())) {
                        itemCount += stack1.getCount();
                    }
                }
                if (itemCount < count)
                    return false;
            }
            return true;
        }

        @Override
        public FurnitureRecipe parent() {
            return parentRecipe;
        }

        @Override
        public ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput inventory, HolderLookup.Provider registryManager) {
            return output.copy();
        }

        @Override
        public ItemStack getRecipeOuput() {
            return output;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof FurnitureInnerRecipe that)) return false;
            return Objects.equals(parentRecipe, that.parentRecipe) && ItemStack.matches(output, that.output) && Objects.equals(combinedIngredients, that.combinedIngredients);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentRecipe, output, combinedIngredients);
        }
    }

    public static class FurnitureOutput {

        public static MapCodec<FurnitureOutput> CODEC = RecordCodecBuilder.mapCodec(furnitureOutputInstance -> furnitureOutputInstance.group(
                Codec.STRING.fieldOf("outputClass").forGetter(out -> out.outputClass),
                Codec.INT.optionalFieldOf("count", 1).forGetter(out -> out.outputCount),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(out -> out.components)
        ).apply(furnitureOutputInstance, FurnitureOutput::new));

        private final String outputClass;
        private final int outputCount;
        private final DataComponentPatch components;

        public FurnitureOutput(String outputClass, int outputCount, DataComponentPatch components) {
            this.outputClass = outputClass;
            this.outputCount = outputCount;
            this.components = components;
        }

        public int getOutputCount() {
            return outputCount;
        }

        public DataComponentPatch getComponents() {
            return components;
        }

        public String getOutputClass() {
            return outputClass;
        }

        public static FurnitureOutput read(RegistryFriendlyByteBuf buf) {
            String outputClass = buf.readUtf();
            int count = buf.readInt();
            DataComponentPatch componentChanges = DataComponentPatch.STREAM_CODEC.decode(buf);
            return new FurnitureOutput(outputClass, count,  componentChanges);
        }

        public static void write(RegistryFriendlyByteBuf buf, FurnitureOutput output) {
            buf.writeUtf(output.outputClass);
            buf.writeInt(output.outputCount);
            DataComponentPatch.STREAM_CODEC.encode(buf, output.components);
        }

        public static Class<? extends Block> getOutputBlockClass(String outputClass) {
            try {
                return (Class<? extends Block>) Class.forName("com.unlikepaladin.pfm.blocks."+outputClass);
            } catch (ClassNotFoundException | ClassCastException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof FurnitureOutput that)) return false;
            return outputCount == that.outputCount && Objects.equals(outputClass, that.outputClass) && Objects.equals(components, that.components);
        }

        @Override
        public int hashCode() {
            return Objects.hash(outputClass, outputCount, components);
        }
    }

    public static final class FurnitureIngredients {
        public static Codec<FurnitureIngredients> CODEC = RecordCodecBuilder.create(furnitureIngredientsInstance -> furnitureIngredientsInstance.group(
                Ingredient.CODEC.listOf().optionalFieldOf("vanillaIngredients", new ArrayList<>()).forGetter(ingredients -> ingredients.vanillaIngredients),
                ExtraCodecs.strictUnboundedMap(Codec.STRING, Codec.INT).fieldOf("variantChildren").forGetter(ingredients -> ingredients.variantChildren)
        ).apply(furnitureIngredientsInstance, FurnitureIngredients::new));

        private final List<Ingredient> vanillaIngredients;
        private final Map<String, Integer> variantChildren;

        public FurnitureIngredients(List<Ingredient> vanillaIngredients, Map<String, Integer> variantChildren) {
            this.vanillaIngredients = vanillaIngredients;
            this.variantChildren = variantChildren;
        }

        public static FurnitureIngredients read(RegistryFriendlyByteBuf buf) {
            List<Ingredient> vanillaIngredients = buf.readCollection(Lists::newArrayListWithCapacity, buf1 -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            Map<String, Integer> variantChildren = buf.readMap((FriendlyByteBuf::readUtf), (FriendlyByteBuf::readInt));
            return new FurnitureIngredients(vanillaIngredients, variantChildren);
        }

        public static void write(RegistryFriendlyByteBuf buf, FurnitureIngredients ingredients) {
            buf.writeCollection(ingredients.vanillaIngredients, ((packetByteBuf, ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.encode((RegistryFriendlyByteBuf) packetByteBuf, ingredient)));
            buf.writeMap(ingredients.variantChildren, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof FurnitureIngredients that)) return false;
            return Objects.equals(vanillaIngredients, that.vanillaIngredients) && Objects.equals(variantChildren, that.variantChildren);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vanillaIngredients, variantChildren);
        }
    }

    public static class Serializer implements RecipeSerializer<DynamicFurnitureRecipe> {
        MapCodec<DynamicFurnitureRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(DynamicFurnitureRecipe::group),
                FurnitureOutput.CODEC.fieldOf("result").forGetter(DynamicFurnitureRecipe::getOutput),
                ResourceLocation.CODEC.listOf().fieldOf("supportedVariants").forGetter(DynamicFurnitureRecipe::getSupportedVariants),
                FurnitureIngredients.CODEC.fieldOf("ingredients").forGetter(DynamicFurnitureRecipe::getInnerIngredients)
        ).apply(instance, DynamicFurnitureRecipe::new));


        public static final StreamCodec<RegistryFriendlyByteBuf, DynamicFurnitureRecipe> PACKET_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read
        );

        @Override
        public MapCodec<DynamicFurnitureRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DynamicFurnitureRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        public static DynamicFurnitureRecipe read(RegistryFriendlyByteBuf buf) {
            String group = buf.readUtf();
            List<ResourceLocation> supportedVariants = buf.readList(FriendlyByteBuf::readResourceLocation);
            FurnitureIngredients ingredients = FurnitureIngredients.read(buf);
            FurnitureOutput output = FurnitureOutput.read(buf);
            return new DynamicFurnitureRecipe(group, output, supportedVariants, ingredients);
        }

        public static void write(RegistryFriendlyByteBuf buf, DynamicFurnitureRecipe recipe) {
            buf.writeUtf(recipe.group);
            buf.writeCollection(recipe.supportedVariants, FriendlyByteBuf::writeResourceLocation);
            FurnitureIngredients.write(buf, recipe.ingredients);
            FurnitureOutput.write(buf, recipe.furnitureOutput);
        }
    }
}
