package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.resources.Identifier;

public class FreezingRecipe extends AbstractCookingRecipe {

    public FreezingRecipe(String group, CookingBookCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(group, category, input, output, experience, cookTime);
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
