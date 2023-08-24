package com.unlikepaladin.pfm.compat.rei.fabric;

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

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;

import com.unlikepaladin.pfm.runtime.data.PFMRecipeProvider;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.utils.CollectionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FurnitureDisplay implements TransferRecipeDisplay {
    protected FurnitureRecipe recipe;
    public static final Identifier IDENTIFIER = new Identifier(PaladinFurnitureMod.MOD_ID, "furniture");
    public List<EntryStack> output;
    public FurnitureDisplay(FurnitureRecipe recipe) {
        this.recipe = recipe;
        output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
    }

    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return CollectionUtils.map(output, Collections::singletonList);
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
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

        return EntryStack.ofIngredients(finalList);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return IDENTIFIER;
    }

    public int getWidth() {
        return getSize(getInputEntries().size());
    }

    public int getHeight() {
        return getSize(getInputEntries().size());
    }

    @Override
    public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
        List<List<EntryStack>> list = Lists.newArrayListWithCapacity(containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container));
        for (int i = 0; i < containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container); i++) {
            list.add(Collections.emptyList());
        }
        List<List<EntryStack>> inputs = getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            List<EntryStack> stacks = inputs.get(i);
            list.set(getSlotWithSize(this, i, containerInfo.getCraftingWidth(container)), stacks);
        }
        return list;
    }

    public static int getSlotWithSize(FurnitureDisplay recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
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