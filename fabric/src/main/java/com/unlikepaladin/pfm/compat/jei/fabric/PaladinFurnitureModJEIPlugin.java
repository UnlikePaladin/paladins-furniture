package com.unlikepaladin.pfm.compat.jei.fabric;
/*
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
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class PaladinFurnitureModJEIPlugin implements IModPlugin {
    public void PaladinFurnitureModJEIPlugin(){

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = Objects.requireNonNull(mc.level);

        List<FreezingRecipe> freezingRecipes = world.recipeAccess().getAllRecipesFor(RecipeTypes.FREEZING_RECIPE).stream().map(RecipeHolder::value).collect(Collectors.toList());;
        registration.addRecipes(PaladinFurnitureModJEI.FREEZING_RECIPE, freezingRecipes);

        List<FurnitureRecipe> furnitureRecipes = world.recipeAccess().getAllRecipesFor(RecipeTypes.FURNITURE_RECIPE).stream().map(RecipeHolder::value).collect(Collectors.toList());
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
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.WHITE_FREEZER), PaladinFurnitureModJEI.FREEZING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.IRON_FREEZER), PaladinFurnitureModJEI.FREEZING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.GRAY_FREEZER), PaladinFurnitureModJEI.FREEZING_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(PaladinFurnitureModBlocksItems.WORKING_TABLE), PaladinFurnitureModJEI.FURNITURE_RECIPE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(PaladinFurnitureModBlocksItems.BASIC_LAMP_ITEM, (ingredient, context)
                -> ingredient.get(PFMComponents.COLOR_COMPONENT).getSerializedName() + "_"+ingredient.get(PFMComponents.VARIANT_COMPONENT).toString());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "jei_plugin");
    }
}*/