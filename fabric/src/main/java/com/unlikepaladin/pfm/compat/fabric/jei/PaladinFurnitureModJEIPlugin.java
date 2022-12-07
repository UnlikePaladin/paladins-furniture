package com.unlikepaladin.pfm.compat.fabric.jei;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@JeiPlugin
public class PaladinFurnitureModJEIPlugin implements IModPlugin {
    public void PaladinFurnitureModJEIPlugin(){

    }

    public static final RecipeType<FurnitureRecipe> FURNITURE_RECIPE =
            RecipeType.create(PaladinFurnitureMod.MOD_ID, "furniture", FurnitureRecipe.class);

    public static final RecipeType<FreezingRecipe> FREEZING_RECIPE =
            RecipeType.create(PaladinFurnitureMod.MOD_ID, "freezing", FreezingRecipe.class);
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld world = Objects.requireNonNull(mc.world);

        List<FreezingRecipe> freezingRecipes = world.getRecipeManager().listAllOfType(RecipeTypes.FREEZING_RECIPE);
        registration.addRecipes(FREEZING_RECIPE, freezingRecipes);

        List<FurnitureRecipe> furnitureRecipes = world.getRecipeManager().listAllOfType(RecipeTypes.FURNITURE_RECIPE);
        registration.addRecipes(FURNITURE_RECIPE, furnitureRecipes);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FurnitureCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FreezingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FRIDGE), FREEZING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.WORKING_TABLE), FURNITURE_RECIPE);
    }

    @Override
    public Identifier getPluginUid() {
        return new Identifier(PaladinFurnitureMod.MOD_ID, "jei_plugin");
    }
}