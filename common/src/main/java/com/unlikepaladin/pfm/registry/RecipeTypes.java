package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.recipe.RecipePropertySet;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resources.ResourceLocation;

public class RecipeTypes {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static RegistryKey<RecipePropertySet> FREEZING_INPUT = RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of(PaladinFurnitureMod.MOD_ID, "freezing"));
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;

    public static RecipeSerializer<SimpleFurnitureRecipe> SIMPLE_FURNITURE_SERIALIZER;
    public static RecipeSerializer<DynamicFurnitureRecipe> DYNAMIC_FURNITURE_SERIALIZER;
    public static RegistryKey<RecipePropertySet> FURNITURE_INPUT = RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of(PaladinFurnitureMod.MOD_ID, "furniture"));

    public static final ResourceLocation FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"furniture");
    public static final ResourceLocation SIMPLE_FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"simple_furniture");
    public static final ResourceLocation DYNAMIC_FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"dynamic_furniture");
    public static final ResourceLocation FREEZING_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"freezing");

}
