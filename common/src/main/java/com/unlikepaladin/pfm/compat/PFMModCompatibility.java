package com.unlikepaladin.pfm.compat;

import net.minecraft.data.recipes.RecipeOutput;

import java.util.Optional;

public interface PFMModCompatibility {
    default void registerBlocks() {};
    default void registerBlockEntityTypes() {};
    default void registerEntityTypes() {};
    default void registerItems() {};
    default void createBlocks() {};
    default void registerScreenHandlers() {};
    default void generateRecipes(RecipeOutput exporter) {};
    default void generateTags() {};
    String getModId();
    default Optional<PFMClientModCompatibility> getClientModCompatiblity() {
        return Optional.empty();
    }
}
