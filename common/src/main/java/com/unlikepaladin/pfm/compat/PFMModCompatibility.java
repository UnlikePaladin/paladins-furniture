package com.unlikepaladin.pfm.compat;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public interface PFMModCompatibility {
    void registerBlocks();
    void registerBlockEntities();
    void registerItems();
    void registerScreenHandlers();
    void registerScreens();
    void generateRecipes(Consumer<RecipeJsonProvider> exporter);
    void generateTags();
    String getModId();
}
