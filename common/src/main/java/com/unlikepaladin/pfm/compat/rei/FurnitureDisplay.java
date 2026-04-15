package com.unlikepaladin.pfm.compat.rei;

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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class FurnitureDisplay implements Display {
    public static final CategoryIdentifier<FurnitureDisplay> IDENTIFIER = CategoryIdentifier.of(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "furniture"));
    private int itemsPerInnerRecipe;
    public List<EntryIngredient> input;
    public List<EntryIngredient> output;
    public Optional<ResourceLocation> location;
    public FurnitureDisplay(RecipeHolder<FurnitureRecipe> recipeEntry, FeatureFlagSet set) {
        this(recipeEntry.value(), set);
        this.location = Optional.of(recipeEntry.id().location());
    }

    public FurnitureDisplay(List<EntryIngredient> input, List<EntryIngredient> output, Optional<ResourceLocation> location, int itemsPerInnerRecipe) {
        this.input = input;
        this.output = output;
        this.location = location;
        this.itemsPerInnerRecipe = itemsPerInnerRecipe;
    }

    public FurnitureDisplay(FurnitureRecipe recipe, FeatureFlagSet set) {
        input = new ArrayList<>();
        output = new ArrayList<>();
        List<EntryIngredient> inputEntries = new ArrayList<>();
        this.itemsPerInnerRecipe = recipe.getMaxInnerRecipeSize();
        for (FurnitureRecipe.CraftableFurnitureRecipe innerRecipe: recipe.getInnerRecipes(set)) {
            Map<Item, Integer> containedItems = innerRecipe.getItemCounts();

            List<EntryIngredient> finalList = new ArrayList<>();
            for (Map.Entry<Item, Integer> entry: containedItems.entrySet()) {
                finalList.add(EntryIngredients.of(new ItemStack(entry.getKey(), entry.getValue())));
            }
            finalList.sort(Comparator.comparing(entryStacks -> entryStacks.getFirst().getValue().toString()));

            if (finalList.size() != itemsPerInnerRecipe) {
                while (finalList.size() != itemsPerInnerRecipe) {
                    finalList.add(EntryIngredient.empty());
                }
            }
            inputEntries.addAll(finalList);
        }
        input.addAll(inputEntries);
        output.addAll(recipe.getInnerRecipes(set).stream().map(FurnitureRecipe.CraftableFurnitureRecipe::getRecipeOuput).map(EntryIngredients::of).toList());
        location = Optional.empty();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    public int itemsPerInnerRecipe() {
        return itemsPerInnerRecipe;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return location;
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }

    public static final DisplaySerializer<FurnitureDisplay> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(FurnitureDisplay::getInputEntries),
                    EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(FurnitureDisplay::getOutputEntries),
                    ResourceLocation.CODEC.optionalFieldOf("location").forGetter(FurnitureDisplay::getDisplayLocation),
                    Codec.INT.fieldOf("itemsPerInnerRecipe").forGetter(FurnitureDisplay::itemsPerInnerRecipe)
            ).apply(instance, FurnitureDisplay::new)),
            StreamCodec.composite(
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    FurnitureDisplay::getInputEntries,
                    EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
                    FurnitureDisplay::getOutputEntries,
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    FurnitureDisplay::getDisplayLocation,
                    ByteBufCodecs.INT,
                    FurnitureDisplay::itemsPerInnerRecipe,
                    FurnitureDisplay::new
            ));
}