package com.unlikepaladin.pfm.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.RawLogTableBlock;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.VariantHelper;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.DyeColor;
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

            CompoundTag outputCompound = furnitureOutput.nbt != null ? furnitureOutput.nbt.copy() : null;

            if (outputCompound != null && outputCompound.contains("color")) {
                optionalOutput = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariantAndColor(variant, DyeColor.byName(outputCompound.getString("color"), DyeColor.WHITE));
                outputCompound.remove("color");
            } else {
                optionalOutput = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariant(variant);
            }
            if (optionalOutput.isEmpty()) continue;

            if (outputCompound != null && outputCompound.contains("variantInNbt") && outputCompound.getBoolean("variantInNbt")) {
                CompoundTag compound;
                 if (outputCompound.contains("BlockEntityTag", Tag.TAG_COMPOUND))
                     compound = outputCompound.getCompound("BlockEntityTag");
                 else {
                     compound = new CompoundTag();
                     outputCompound.put("BlockEntityTag", compound);
                 }
                 outputCompound.remove("variantInNbt");
                 compound.putString("variant", id.toString());
            }
            ItemStack output = new ItemStack(optionalOutput.get().asItem(), furnitureOutput.getOutputCount());
            if (outputCompound != null && !outputCompound.isEmpty())
                output.setTag(outputCompound.copy());

            Map<String, Integer> childrenToCountMap = ingredients.variantChildren;

            boolean abortVariant = false;
            List<Ingredient> stacks = Lists.newArrayList();
            for (Map.Entry<String, Integer> entry : childrenToCountMap.entrySet()) {
                ItemLike convertible = variant.getItemForRecipe(entry.getKey(), getOutputBlockClass());
                if (convertible == null || convertible.asItem() == Items.AIR){
                    abortVariant = true;
                    break;
                }
                stacks.add(Ingredient.of(new ItemStack(convertible.asItem(), entry.getValue())));
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
                List<Ingredient> strippedIngredients = Lists.newArrayList();
                for (Map.Entry<String, Integer> entry : childrenToCountMap.entrySet()) {
                    strippedIngredients.add(Ingredient.of(new ItemStack(woodVariant.getItemForRecipe(entry.getKey(), getOutputBlockClass(), true), entry.getValue())));
                }
                if (getOutputBlockClass() == RawLogTableBlock.class) {
                    strippedIngredients.set(0, Ingredient.of((Block)woodVariant.getChild("stripped_log")));
                }

                Optional<Block> strippedOptional = PaladinFurnitureMod.furnitureEntryMap.get(getOutputBlockClass()).getEntryFromVariant(variant, true);
                if (strippedOptional.isPresent()) {

                    ItemStack strippedOutput = new ItemStack(strippedOptional.get(), furnitureOutput.getOutputCount());
                    if (outputCompound != null  && !outputCompound.isEmpty())
                        output.setTag(outputCompound.copy());

                    FurnitureInnerRecipe stripped = new FurnitureInnerRecipe(this, strippedOutput, strippedIngredients);
                    recipes.add(stripped);
                }
            }
            furnitureInnerRecipes.put(id, recipes);
        }
    }

    @Override
    public boolean matches(Inventory inventory, Level level) {
        constructInnerRecipes();

        for (ResourceLocation id : furnitureInnerRecipes.keySet()) {
            List<FurnitureInnerRecipe> recipes = furnitureInnerRecipes.get(id);
            for (FurnitureInnerRecipe recipe : recipes) {
                if (recipe.matches(inventory, level))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<CraftableFurnitureRecipe> getAvailableOutputs(Inventory inventory, RegistryAccess registryManager) {
        constructInnerRecipes();
        List<CraftableFurnitureRecipe> stacks = Lists.newArrayList();
        for (ResourceLocation id : furnitureInnerRecipes.keySet()) {
            List<FurnitureInnerRecipe> recipes = furnitureInnerRecipes.get(id);
            for (FurnitureInnerRecipe recipe : recipes) {
                if (recipe.matches(inventory, inventory.player.level()))
                    stacks.add(recipe);
            }
        }
        return stacks;
    }

    @Override
    public List<CraftableFurnitureRecipe> getInnerRecipes() {
        constructInnerRecipes();
        List<CraftableFurnitureRecipe> outputs = new ArrayList<>();
        for (List<FurnitureInnerRecipe> recipes : furnitureInnerRecipes.values())
            outputs.addAll(recipes);
        return outputs;
    }

    @Override
    public String outputClass() {
        return furnitureOutput.outputClass;
    }

    @Override
    public ItemStack assemble(Inventory inventory, RegistryAccess registryManager) {
        PaladinFurnitureMod.GENERAL_LOGGER.debug("Something has tried to craft a dynamic furniture recipe without context");
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        PaladinFurnitureMod.GENERAL_LOGGER.debug("Something has tried to get the output of a dynamic furniture recipe without context");
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.DYNAMIC_FURNITURE_SERIALIZER;
    }

    protected Class<? extends Block> getOutputBlockClass() {
        try {
            return (Class<? extends Block>) Class.forName("com.unlikepaladin.pfm.blocks."+furnitureOutput.getOutputClass());
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ResourceLocation> getSupportedVariants() {
        return supportedVariants;
    }

    @Override
    public int getOutputCount(RegistryAccess registryManager) {
        return furnitureOutput.getOutputCount();
    }

    @Override
    public CraftableFurnitureRecipe getInnerRecipeFromOutput(ItemStack stack) {
        constructInnerRecipes();
        if(outputToInnerRecipe.containsKey(stack)) {
            return outputToInnerRecipe.get(stack);
        }
        return outputItemToInnerRecipe.get(stack.getItem());
    }

    @Override
    public int getMaxInnerRecipeSize() {
        return ingredients.vanillaIngredients.size()+ingredients.variantChildren.size();
    }

    @Override
    public List<? extends CraftableFurnitureRecipe> getInnerRecipesForVariant(ResourceLocation identifier){
        constructInnerRecipes();
        if (furnitureInnerRecipes.containsKey(identifier)) {
            return furnitureInnerRecipes.get(identifier);
        }
        return List.of();
    }


    @Override
    public String getName(RegistryAccess registryManager) {
        return outputClass().replaceAll("(?<=[a-z])(?=[A-Z])", " ");
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
        public ItemStack getResultItem(RegistryAccess registryManager) {
            return output;
        }

        @Override
        public List<Ingredient> getIngredients() {
            return combinedIngredients;
        }

        @Override
        public boolean matches(Inventory inventory, Level level) {
            List<Ingredient> allIngredients = getIngredients();
            BitSet hasIngredient = new BitSet(allIngredients.size());
            for (int i = 0; i < allIngredients.size(); i++) {
                Ingredient ingredient = allIngredients.get(i);
                for (ItemStack stack : ingredient.getItems()) {
                    int countInInventory = inventory.countItem(stack.getItem());
                    if (countInInventory >= stack.getCount()) {
                        hasIngredient.set(i, true);
                        break;
                    }
                }
            }

            // Compares the numbers of bits that are true to the size
            return hasIngredient.cardinality() == allIngredients.size();
        }

        @Override
        public FurnitureRecipe parent() {
            return parentRecipe;
        }

        @Override
        public ItemStack assemble(Inventory inventory, RegistryAccess registryManager) {
            return output.copy();
        }

        @Override
        public ItemStack getRecipeOuput() {
            return output;
        }
    }

    public static class FurnitureOutput {

        public static Codec<FurnitureOutput> CODEC = RecordCodecBuilder.create(furnitureOutputInstance -> furnitureOutputInstance.group(
                Codec.STRING.fieldOf("outputClass").forGetter(out -> out.outputClass),
                Codec.INT.optionalFieldOf("count", 1).forGetter(out -> out.outputCount),
                CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(out -> out.nbt)
        ).apply(furnitureOutputInstance, FurnitureOutput::new));

        private final String outputClass;
        private final int outputCount;
        private final CompoundTag nbt;

        private FurnitureOutput(String outputClass, int outputCount, CompoundTag nbt) {
            this.outputClass = outputClass;
            this.outputCount = outputCount;
            this.nbt = nbt;
        }

        public int getOutputCount() {
            return outputCount;
        }

        public CompoundTag getTag() {
            return nbt;
        }

        public String getOutputClass() {
            return outputClass;
        }

        public static FurnitureOutput read(FriendlyByteBuf buf) {
            String outputClass = buf.readUtf();
            int count = buf.readInt();
            CompoundTag nbt = buf.readNbt();
            return new FurnitureOutput(outputClass, count, nbt);
        }

        public static void write(FriendlyByteBuf buf, FurnitureOutput output) {
            buf.writeUtf(output.outputClass);
            buf.writeInt(output.outputCount);
            buf.writeNbt(output.nbt);
        }

    }

    public static final class FurnitureIngredients {
        public static Codec<FurnitureIngredients> CODEC = RecordCodecBuilder.create(furnitureIngredientsInstance -> furnitureIngredientsInstance.group(
                Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("vanillaIngredients", new ArrayList<>()).forGetter(ingredients -> ingredients.vanillaIngredients),
                ExtraCodecs.strictUnboundedMap(Codec.STRING, Codec.INT).fieldOf("variantChildren").forGetter(ingredients -> ingredients.variantChildren)
        ).apply(furnitureIngredientsInstance, FurnitureIngredients::new));

        private final List<Ingredient> vanillaIngredients;
        private final Map<String, Integer> variantChildren;

        public FurnitureIngredients(List<Ingredient> vanillaIngredients, Map<String, Integer> variantChildren) {
            this.vanillaIngredients = vanillaIngredients;
            this.variantChildren = variantChildren;
        }

        public static FurnitureIngredients read(FriendlyByteBuf buf) {
            List<Ingredient> vanillaIngredients = buf.readCollection(Lists::newArrayListWithCapacity, Ingredient::fromNetwork);
            Map<String, Integer> variantChildren = buf.readMap((FriendlyByteBuf::readUtf), (FriendlyByteBuf::readInt));
            return new FurnitureIngredients(vanillaIngredients, variantChildren);
        }

        public static void write(FriendlyByteBuf buf, FurnitureIngredients ingredients) {
            buf.writeCollection(ingredients.vanillaIngredients, ((packetByteBuf, ingredient) -> ingredient.toNetwork(buf)));
            buf.writeMap(ingredients.variantChildren, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        }

    }

    public static class Serializer implements RecipeSerializer<DynamicFurnitureRecipe> {
        Codec<DynamicFurnitureRecipe> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(DynamicFurnitureRecipe::getGroup),
                FurnitureOutput.CODEC.fieldOf("result").forGetter(recipe -> recipe.furnitureOutput),
                ResourceLocation.CODEC.listOf().fieldOf("supportedVariants").forGetter(DynamicFurnitureRecipe::getSupportedVariants),
                FurnitureIngredients.CODEC.fieldOf("ingredients").forGetter(recipe -> recipe.ingredients)
        ).apply(instance, DynamicFurnitureRecipe::new));

        @Override
        public Codec<DynamicFurnitureRecipe> codec() {
            return CODEC;
        }

        @Override
        public DynamicFurnitureRecipe fromNetwork(FriendlyByteBuf buf) {
            String group = buf.readUtf();
            List<ResourceLocation> supportedVariants = buf.readList(FriendlyByteBuf::readResourceLocation);
            FurnitureIngredients ingredients = FurnitureIngredients.read(buf);
            FurnitureOutput output = FurnitureOutput.read(buf);
            return new DynamicFurnitureRecipe(group, output, supportedVariants, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, DynamicFurnitureRecipe recipe) {
            buf.writeUtf(recipe.group);
            buf.writeCollection(recipe.supportedVariants, FriendlyByteBuf::writeResourceLocation);
            FurnitureIngredients.write(buf, recipe.ingredients);
            FurnitureOutput.write(buf, recipe.furnitureOutput);
        }
    }
}
