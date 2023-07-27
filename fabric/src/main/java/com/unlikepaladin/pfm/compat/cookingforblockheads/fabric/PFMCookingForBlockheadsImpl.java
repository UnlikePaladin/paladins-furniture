package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class PFMCookingForBlockheadsImpl extends PFMCookingForBlockheads {
    @Override
    public void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
    }

    @Override
    public void generateTags() {
    }

    @Override
    public String getModId() {
        return "cookingforblockheads";
    }

    @Override
    public void registerBlocks() {
    }

    @Override
    public void registerBlockEntities() {
    }

    @Override
    public void registerItems() {
    }

    @Override
    public void registerScreenHandlers() {
    }

    @Override
    public void registerScreens() {
    }

    public static PFMCookingForBlockheads getInstance() {
        return new PFMCookingForBlockheadsImpl();
    }
}
