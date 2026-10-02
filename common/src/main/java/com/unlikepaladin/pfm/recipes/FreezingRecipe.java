package com.unlikepaladin.pfm.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.resources.Identifier;

public class FreezingRecipe extends AbstractCookingRecipe {

    public FreezingRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    public static MapCodec<FreezingRecipe> cookingMapCodec(int defaultCookingTime) {
        return RecordCodecBuilder.mapCodec(
            i -> i.group(
                CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                CookingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(SingleItemRecipe::result),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(AbstractCookingRecipe::experience),
                Codec.INT.optionalFieldOf("cookingtime", defaultCookingTime).forGetter(AbstractCookingRecipe::cookingTime)
            ).apply(i, FreezingRecipe::new)
        );
    }

    @Override
    public RecipeSerializer<? extends AbstractCookingRecipe> getSerializer() {
        return RecipeTypes.FREEZING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return RecipeTypes.FREEZING_RECIPE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }

    @Override
    protected Item furnaceIcon() {
        return PaladinFurnitureModBlocksItems.WHITE_FREEZER.asItem();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
