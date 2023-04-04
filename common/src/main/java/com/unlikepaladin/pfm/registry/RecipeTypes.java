package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.NewFurnitureRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class RecipeTypes {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;
    public static RecipeType<NewFurnitureRecipe> NEW_FURNITURE_RECIPE;

    public static RecipeSerializer<FurnitureRecipe> FURNITURE_SERIALIZER;
    public static RecipeSerializer<NewFurnitureRecipe> NEW_FURNITURE_SERIALIZER;

    public static final Identifier FURNITURE_ID = new Identifier(PaladinFurnitureMod.MOD_ID,"furniture");
    public static final Identifier NEW_FURNITURE_ID = new Identifier(PaladinFurnitureMod.MOD_ID,"newfurniture");

}
