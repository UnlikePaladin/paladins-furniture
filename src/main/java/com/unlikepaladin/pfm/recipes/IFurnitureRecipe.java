package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.registry.RecipeRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface IFurnitureRecipe extends Recipe<CraftingInventory> {
        @Override
        default RecipeType<?> getType() {
            return RecipeRegistry.FURNITURE_RECIPE;
        }
}
