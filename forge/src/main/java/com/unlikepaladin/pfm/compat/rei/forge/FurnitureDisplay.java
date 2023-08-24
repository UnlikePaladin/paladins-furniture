package com.unlikepaladin.pfm.compat.rei.forge;

/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.InputIngredient;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FurnitureDisplay extends BasicDisplay implements SimpleGridMenuDisplay {
    protected FurnitureRecipe recipe;
    public static final CategoryIdentifier<FurnitureDisplay> IDENTIFIER = CategoryIdentifier.of(new Identifier(PaladinFurnitureMod.MOD_ID, "furniture"));
    public List<EntryIngredient> output;
    public FurnitureDisplay(FurnitureRecipe recipe) {
        super(Collections.emptyList(), Collections.singletonList(EntryIngredients.of(recipe.getOutput())));
        this.recipe = recipe;
        output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        List<Ingredient> ingredients = recipe.getIngredients();
        HashMap<Item, Integer> containedItems = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            for (ItemStack stack : PFMRecipeProvider.pfm$getMatchingStacks(ingredient)) {
                if (!containedItems.containsKey(stack.getItem())) {
                    containedItems.put(stack.getItem(), 1);
                } else {
                    containedItems.put(stack.getItem(), containedItems.get(stack.getItem()) + 1);
                }
            }
        }
        List<Ingredient> finalList = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry: containedItems.entrySet()) {
            finalList.add(Ingredient.ofStacks(new ItemStack(entry.getKey(), entry.getValue())));
        }
        finalList.sort(Comparator.comparing(o -> PFMRecipeProvider.pfm$getMatchingStacks(o)[0].getItem().toString()));

        return EntryIngredients.ofIngredients(finalList);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }


    @Override
    public int getWidth() {
        return getSize(getInputEntries().size());
    }

    @Override
    public int getHeight() {
        return getSize(getInputEntries().size());
    }

    private static int getSize(int total) {
        if (total > 4) {
            return 3;
        } else if (total > 1) {
            return 2;
        } else {
            return 1;
        }
    }
}