package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public class FreezingRecipe extends AbstractCookingRecipe {

    public FreezingRecipe(ResourceLocation id, String group, CookingBookCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(RecipeTypes.FREEZING_RECIPE, id, group, category, input, output, experience, cookTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.FREEZING_RECIPE_SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
