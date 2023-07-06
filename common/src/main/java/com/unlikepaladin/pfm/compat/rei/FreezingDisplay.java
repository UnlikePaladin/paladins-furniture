package com.unlikepaladin.pfm.compat.rei;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class FreezingDisplay implements Display {
    public static final CategoryIdentifier<FreezingDisplay> IDENTIFIER = CategoryIdentifier.of(new Identifier(PaladinFurnitureMod.MOD_ID, "freezing"));

    public List<EntryIngredient> input;
    public List<EntryIngredient> output;
    public int cookTime;
    private final float xp;

    public FreezingDisplay(FreezingRecipe recipe) {
        input = EntryIngredients.ofIngredients(recipe.getIngredients());
        output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
        cookTime = recipe.getCookTime();
        xp = recipe.getExperience();
    }
    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }


    public float getXp() {
        return xp;
    }
}
