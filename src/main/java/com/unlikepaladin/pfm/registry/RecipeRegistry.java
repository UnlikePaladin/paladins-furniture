package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureSerializer;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.unlikepaladin.pfm.PaladinFurnitureMod.MOD_ID;

public class RecipeRegistry {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;
    public static RecipeSerializer<FurnitureRecipe> FURNITURE_SERIALIZER;
    public static final Identifier FURNITURE_ID = new Identifier(MOD_ID,"furniture");

    public static void registerRecipes() {
        FREEZING_RECIPE = Registry.register(Registry.RECIPE_TYPE, MOD_ID + ":freezing",  new RecipeType<FreezingRecipe>() {
            @Override
            public String toString() {return "freezing";}
        });
        FREEZING_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "freezing"), new CookingRecipeSerializer<>(FreezingRecipe::new, 200));
        FURNITURE_RECIPE = Registry.register(Registry.RECIPE_TYPE, FURNITURE_ID,  new RecipeType<FurnitureRecipe>() {
            @Override
            public String toString() {return "furniture";}
        });
        FURNITURE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID,"furniture"), new FurnitureSerializer());
    }
}
