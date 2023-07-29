package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class FreezingRecipe extends AbstractCookingRecipe {

    public FreezingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(RecipeTypes.FREEZING_RECIPE, id, group, input, output, experience, cookTime);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FREEZER);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.FREEZING_RECIPE_SERIALIZER;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }
}
