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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.Identifier;
import mezz.jei.common.Internal;
import net.minecraft.world.item.crafting.RecipeMap;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class PaladinFurnitureModJEIPlugin implements IModPlugin {
    public void PaladinFurnitureModJEIPlugin(){

    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeMap clientSyncedRecipes = Internal.getClientSyncedRecipes();

        if (clientSyncedRecipes.values().isEmpty()) {
            return;
        }

        List<FreezingRecipe> freezingRecipes = clientSyncedRecipes.byType(RecipeTypes.FREEZING_RECIPE).stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(PaladinFurnitureModJEI.FREEZING_RECIPE, freezingRecipes);

        List<FurnitureRecipe> furnitureRecipes = clientSyncedRecipes.byType(RecipeTypes.FURNITURE_RECIPE).stream().map(RecipeHolder::value).collect(Collectors.toList());;
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
                -> ingredient.get(PFMComponents.COLOR_COMPONENT).getSerializedName() + "_"+ingredient.get(PFMComponents.VARIANT_COMPONENT).toString());
        registration.registerFromDataComponentTypes(PaladinFurnitureModBlocksItems.OFFICE_CHAIR_ITEM, PFMComponents.COLOR_COMPONENT);
    }

    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "jei_plugin");
    }
}