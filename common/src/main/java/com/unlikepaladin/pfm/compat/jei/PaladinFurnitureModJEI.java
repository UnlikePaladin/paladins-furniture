package com.unlikepaladin.pfm.compat.jei;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import mezz.jei.api.recipe.types.IRecipeType;

public class PaladinFurnitureModJEI {
    public static final IRecipeType<FurnitureRecipe> FURNITURE_RECIPE =
            IRecipeType.create(PaladinFurnitureMod.MOD_ID, "furniture", FurnitureRecipe.class);

    public static final IRecipeType<FreezingRecipe> FREEZING_RECIPE =
            IRecipeType.create(PaladinFurnitureMod.MOD_ID, "freezing", FreezingRecipe.class);
}