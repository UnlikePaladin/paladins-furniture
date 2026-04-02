package com.unlikepaladin.pfm.compat.rei;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FreezingDisplay implements Display {
    public static final CategoryIdentifier<FreezingDisplay> IDENTIFIER = CategoryIdentifier.of(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "freezing"));

    private final RecipeHolder<FreezingRecipe> recipeEntry;
    public List<EntryIngredient> input;
    public List<EntryIngredient> output;
    public int cookTime;
    private final float xp;

    public FreezingDisplay(RecipeHolder<FreezingRecipe> recipe) {
        input = EntryIngredients.ofIngredients(recipe.value().getIngredients());
        output = Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess())));
        cookTime = recipe.value().getCookingTime();
        xp = recipe.value().getExperience();
        recipeEntry = recipe;
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

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.of(recipeEntry.id());
    }
}
