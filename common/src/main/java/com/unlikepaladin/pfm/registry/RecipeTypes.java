package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class RecipeTypes {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;

    public static RecipeSerializer<FurnitureRecipe> FURNITURE_SERIALIZER;

    public static final Identifier FURNITURE_ID = Identifier.of(PaladinFurnitureMod.MOD_ID,"furniture");

}
