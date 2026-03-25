package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.compat.cookingforblockheads.PFMCookingForBlockheads;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class PFMCookingForBlockheadsImpl extends PFMCookingForBlockheads {
    @Override
    public void generateRecipes(Consumer<FinishedRecipe> exporter) {
    }

    @Override
    public String getModId() {
        return "cookingforblockheads";
    }

    @Override
    public void registerBlocks() {
    }

    public static PFMCookingForBlockheads getInstance() {
        return new PFMCookingForBlockheadsImpl();
    }
}
