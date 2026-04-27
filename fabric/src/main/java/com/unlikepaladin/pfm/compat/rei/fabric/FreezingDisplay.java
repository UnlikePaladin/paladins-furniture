package com.unlikepaladin.pfm.compat.rei.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FreezingDisplay extends DefaultCookingDisplay {

    public FreezingDisplay(FreezingRecipe recipe) {
        super(recipe);
    }

    @Override
    public @NotNull ResourceLocation getRecipeCategory() {
        return IDENTIFIER;
    }
    public static ResourceLocation IDENTIFIER = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "plugins/freezing");
}