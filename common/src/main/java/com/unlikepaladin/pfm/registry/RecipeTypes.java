package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.Identifier;

public class RecipeTypes {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static ResourceKey<RecipePropertySet> FREEZING_INPUT = ResourceKey.create(RecipePropertySet.TYPE_KEY, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "freezing"));
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;

    public static RecipeSerializer<SimpleFurnitureRecipe> SIMPLE_FURNITURE_SERIALIZER;
    public static RecipeSerializer<DynamicFurnitureRecipe> DYNAMIC_FURNITURE_SERIALIZER;
    public static ResourceKey<RecipePropertySet> FURNITURE_INPUT = ResourceKey.create(RecipePropertySet.TYPE_KEY, Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture"));

    public static final Identifier FURNITURE_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"furniture");
    public static final Identifier SIMPLE_FURNITURE_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"simple_furniture");
    public static final Identifier DYNAMIC_FURNITURE_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"dynamic_furniture");
    public static final Identifier FREEZING_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"freezing");

}
