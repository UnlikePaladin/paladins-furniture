package com.unlikepaladin.pfm.compat.fabric.rei;

import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class PaladinFurnitureModREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FurnitureCategory());
        registry.addWorkstations(FurnitureDisplay.IDENTIFIER, FurnitureCategory.ICON);
        registry.add(new FreezingCategory());
        registry.addWorkstations(FreezingDisplay.IDENTIFIER, FreezingCategory.ICON);

    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FurnitureRecipe.class, RecipeTypes.FURNITURE_RECIPE, FurnitureDisplay::of);
        registry.registerFiller(FreezingRecipe.class, FreezingDisplay::new);
    }

}
