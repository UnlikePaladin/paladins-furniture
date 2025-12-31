package com.unlikepaladin.pfm.compat.jei.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.jei.FreezingCategory;
import com.unlikepaladin.pfm.compat.jei.FurnitureCategory;
import com.unlikepaladin.pfm.compat.jei.PaladinFurnitureModJEI;
import com.unlikepaladin.pfm.items.PFMComponents;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.common.Internal;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.PreparedRecipes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@JeiPlugin
public class PaladinFurnitureModJEIPlugin implements IModPlugin {
    public void PaladinFurnitureModJEIPlugin(){

    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        PreparedRecipes clientSyncedRecipes = Internal.getClientSyncedRecipes();

        if (clientSyncedRecipes.recipes().isEmpty()) {
            return;
        }

        List<FreezingRecipe> freezingRecipes = clientSyncedRecipes.getAll(RecipeTypes.FREEZING_RECIPE).stream().map(RecipeEntry::value).collect(Collectors.toList());
        registration.addRecipes(PaladinFurnitureModJEI.FREEZING_RECIPE, freezingRecipes);

        List<FurnitureRecipe> furnitureRecipes = clientSyncedRecipes.getAll(RecipeTypes.FURNITURE_RECIPE).stream().map(RecipeEntry::value).collect(Collectors.toList());;
        registration.addRecipes(PaladinFurnitureModJEI.FURNITURE_RECIPE, furnitureRecipes);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FurnitureCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FreezingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addCraftingStation(PaladinFurnitureModJEI.FREEZING_RECIPE, new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FREEZER));
        registration.addCraftingStation(PaladinFurnitureModJEI.FREEZING_RECIPE, new ItemStack(PaladinFurnitureModBlocksItems.IRON_FREEZER));
        registration.addCraftingStation(PaladinFurnitureModJEI.FREEZING_RECIPE, new ItemStack(PaladinFurnitureModBlocksItems.GRAY_FREEZER));
        registration.addCraftingStation(PaladinFurnitureModJEI.FURNITURE_RECIPE, new ItemStack(PaladinFurnitureModBlocksItems.WORKING_TABLE));
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM, (ingredient, context)
                -> ingredient.get(PFMComponents.COLOR_COMPONENT).asString() + "_"+ingredient.get(PFMComponents.VARIANT_COMPONENT).toString());
        registration.registerFromDataComponentTypes(PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM, PFMComponents.COLOR_COMPONENT);
    }

    @Override
    public Identifier getPluginUid() {
        return Identifier.of(PaladinFurnitureMod.MOD_ID, "jei_plugin");
    }
}