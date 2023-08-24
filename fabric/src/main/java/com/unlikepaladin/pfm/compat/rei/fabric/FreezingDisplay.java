package com.unlikepaladin.pfm.compat.rei.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.rei.plugin.DefaultPlugin;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FreezingDisplay extends DefaultCookingDisplay {

    public FreezingDisplay(FreezingRecipe recipe) {
        super(recipe);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return IDENTIFIER;
    }
    public static Identifier IDENTIFIER = new Identifier(PaladinFurnitureMod.MOD_ID, "plugins/freezing");
}