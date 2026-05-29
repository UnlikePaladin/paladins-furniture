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
import net.minecraft.resources.ResourceLocation;

public class RecipeTypes {
    public static RecipeType<FreezingRecipe> FREEZING_RECIPE;
    public static ResourceKey<RecipePropertySet> FREEZING_INPUT = ResourceKey.create(RecipePropertySet.TYPE_KEY, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "freezing"));
    public static RecipeSerializer<FreezingRecipe> FREEZING_RECIPE_SERIALIZER;

    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE;

    public static RecipeSerializer<SimpleFurnitureRecipe> SIMPLE_FURNITURE_SERIALIZER;
    public static RecipeSerializer<DynamicFurnitureRecipe> DYNAMIC_FURNITURE_SERIALIZER;
    public static ResourceKey<RecipePropertySet> FURNITURE_INPUT = ResourceKey.create(RecipePropertySet.TYPE_KEY, ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture"));

    public static final ResourceLocation FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"furniture");
    public static final ResourceLocation SIMPLE_FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"simple_furniture");
    public static final ResourceLocation DYNAMIC_FURNITURE_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"dynamic_furniture");
    public static final ResourceLocation FREEZING_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"freezing");

}
