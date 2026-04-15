package com.unlikepaladin.pfm.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;

public class SimpleFurnitureRecipe implements FurnitureRecipe, FurnitureRecipe.CraftableFurnitureRecipe {
    final String group;
    final ItemStack output;
    final NonNullList<Ingredient> input;


    public SimpleFurnitureRecipe(String group, ItemStack output, List<Ingredient> input) {
        this.group = group;
        this.output = output;
        this.input = NonNullList.createWithCapacity(input.size());
        this.input.addAll(input);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean matches(FurnitureRecipe.FurnitureRecipeInput input, Level world) {
        Map<Item, Integer> ingredientCounts = getItemCounts();
        for (Map.Entry<Item, Integer> entry : ingredientCounts.entrySet()) {
            Item item = entry.getKey();
            Integer count = entry.getValue();

            int itemCount = 0;
            ItemStack defaultStack = item.getDefaultInstance();
            for (ItemStack stack1 : input.playerInventory().items) {
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
        return this;
    }

    @Override
    public ItemStack getRecipeOuput() {
        return output;
    }

    @Override
    public ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput playerInventory, HolderLookup.Provider registryManager) {
        if (!this.output.getComponents().isEmpty() && output.has(DataComponents.BLOCK_ENTITY_DATA) && output.get(DataComponents.BLOCK_ENTITY_DATA).isEmpty()) {
            ItemStack stack = this.output.copy();
            stack.remove(DataComponents.BLOCK_ENTITY_DATA);
            return stack;
        }
        return this.output.copy();
    }

    @Override
    public String group() {
        return this.group;
    }

    @Override
    public ItemStack getResult(HolderLookup.Provider registryManager) {
        return this.output;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        Serializer.write(buf, this);
    }

    @Override
    public RecipeSerializer<? extends Recipe<FurnitureRecipeInput>> getSerializer() {
        return RecipeTypes.SIMPLE_FURNITURE_SERIALIZER;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.createFromOptionals(input.stream().map(Optional::of).toList());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public String outputClass() {
        return this.output.getItem().getClass().getSimpleName();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public List<CraftableFurnitureRecipe> getInnerRecipes(FeatureFlagSet featureSet) {
        return Collections.singletonList(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SimpleFurnitureRecipe that)) return false;
        return Objects.equals(group, that.group) && Objects.equals(output, that.output) && Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, output, input);
    }

    public static class Serializer
            implements RecipeSerializer<SimpleFurnitureRecipe> {

        private static final MapCodec<SimpleFurnitureRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(SimpleFurnitureRecipe::group),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                                Ingredient.CODEC.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
                                    NonNullList<Ingredient> defaultedList = NonNullList.create();
                                    defaultedList.addAll(ingredients);
                                    if (defaultedList.isEmpty()) {
                                        return DataResult.error(() -> "No ingredients for furniture recipe");
                                    } else {
                                        return DataResult.success(defaultedList);
                                    }
                                }, DataResult::success).forGetter(SimpleFurnitureRecipe::getIngredients))
                        .apply(instance, SimpleFurnitureRecipe::new));

        @Override
        public MapCodec<SimpleFurnitureRecipe> codec() {
            return CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, SimpleFurnitureRecipe> PACKET_CODEC = StreamCodec.of(
                SimpleFurnitureRecipe.Serializer::write, SimpleFurnitureRecipe.Serializer::read
        );
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SimpleFurnitureRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        public static SimpleFurnitureRecipe read(RegistryFriendlyByteBuf packetByteBuf) {
            String string = packetByteBuf.readUtf();
            NonNullList<Ingredient> defaultedList = packetByteBuf.readCollection(NonNullList::createWithCapacity, buf1 -> Ingredient.CONTENTS_STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf1));
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(packetByteBuf);
            return new SimpleFurnitureRecipe(string, itemStack, defaultedList);
        }

        public static void write(RegistryFriendlyByteBuf packetByteBuf, SimpleFurnitureRecipe simpleFurnitureRecipe) {
            packetByteBuf.writeUtf(simpleFurnitureRecipe.group);
            packetByteBuf.writeCollection(simpleFurnitureRecipe.input, (buff, ingredient) -> Ingredient.CONTENTS_STREAM_CODEC.encode((RegistryFriendlyByteBuf) buff, ingredient));
            ItemStack.STREAM_CODEC.encode(packetByteBuf, simpleFurnitureRecipe.output);
        }
    }
}
