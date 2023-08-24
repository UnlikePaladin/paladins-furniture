package com.unlikepaladin.pfm.compat.rei.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import net.minecraft.util.Identifier;

public class FreezingDisplay extends DefaultCookingDisplay {

    public FreezingDisplay(FreezingRecipe recipe) {
        super(recipe);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }
    public static CategoryIdentifier<FreezingDisplay> IDENTIFIER = CategoryIdentifier.of(new Identifier(PaladinFurnitureMod.MOD_ID, "plugins/freezing"));
}