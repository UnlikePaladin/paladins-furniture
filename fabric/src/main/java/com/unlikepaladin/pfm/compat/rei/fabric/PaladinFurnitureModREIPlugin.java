package com.unlikepaladin.pfm.compat.rei.fabric;
/*
import com.unlikepaladin.pfm.compat.rei.FreezingCategory;
import com.unlikepaladin.pfm.compat.rei.FreezingDisplay;
import com.unlikepaladin.pfm.compat.rei.FurnitureCategory;
import com.unlikepaladin.pfm.compat.rei.FurnitureDisplay;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.plugin.campfire.DefaultCampfireCategory;
import me.shedaniel.rei.plugin.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.crafting.DefaultShapedDisplay;
import me.shedaniel.rei.plugin.smoking.DefaultSmokingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.SmokerScreen;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.util.Identifier;


public class PaladinFurnitureModREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FurnitureCategory());
        registry.addWorkstations(FurnitureDisplay.IDENTIFIER, FurnitureCategory.ICON);
        registry.add(new FreezingCategory());
        registry.addWorkstations(FreezingDisplay.IDENTIFIER, FreezingCategory.WORKSTATIONS);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(FurnitureRecipe.class, FurnitureDisplay::new);
        registry.registerFiller(FreezingRecipe.class, FreezingDisplay::new);
    }

    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier("pfm:pfm");
    }
}
*/