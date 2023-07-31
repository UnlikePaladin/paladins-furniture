package com.unlikepaladin.pfm.compat;

import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.Optional;
import java.util.function.Consumer;

public interface PFMModCompatibility {
    default void registerBlocks() {};
    default void registerBlockEntityTypes() {};
    default void registerEntityTypes() {};
    default void registerItems() {};
    default void registerScreenHandlers() {};
    default void generateRecipes(Consumer<RecipeJsonProvider> exporter) {};
    default void generateTags() {};
    String getModId();
    default Optional<PFMClientModCompatibility> getClientModCompatiblity() {
        return Optional.empty();
    }
}
