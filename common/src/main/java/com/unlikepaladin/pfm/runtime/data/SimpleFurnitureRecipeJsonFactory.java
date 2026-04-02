package com.unlikepaladin.pfm.runtime.data;


import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleFurnitureRecipeJsonFactory implements RecipeBuilder {
    private final Item output;
    private final int outputCount;
    private final NonNullList<Ingredient> inputs = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private boolean showNotification = true;
    private boolean emptyCriterion = true;

    @NotNull
    private CompoundTag nbtElement;
    @Nullable
    private String group;

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount) {
        this.output = output.asItem();
        this.outputCount = outputCount;
        this.nbtElement = new CompoundTag();
    }

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount, CompoundTag nbtElement) {
        this.output = output.asItem();
        this.outputCount = outputCount;
        this.nbtElement = nbtElement;
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count, CompoundTag nbtElement) {
        return new SimpleFurnitureRecipeJsonFactory(output, count, nbtElement);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, CompoundTag nbtElement) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1, nbtElement);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1);
    }
    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count) {
        return new SimpleFurnitureRecipeJsonFactory(output, count);
    }
    public SimpleFurnitureRecipeJsonFactory input(TagKey<Item> tag) {
        return this.input(Ingredient.of(tag));
    }

    public SimpleFurnitureRecipeJsonFactory input(ItemLike itemProvider) {
        return this.input(itemProvider, 1);
    }

    public SimpleFurnitureRecipeJsonFactory input(ItemLike itemProvider, int size) {
        for (int i = 0; i < size; ++i) {
            this.input(Ingredient.of(itemProvider));
        }
        return this;
    }

    public SimpleFurnitureRecipeJsonFactory input(Ingredient ingredient) {
        return this.input(ingredient, 1);
    }

    public SimpleFurnitureRecipeJsonFactory input(Ingredient ingredient, int size) {
        for (int i = 0; i < size; ++i) {
            this.inputs.add(ingredient);
        }
        return this;
    }

    @Override
    public SimpleFurnitureRecipeJsonFactory unlockedBy(String name, Criterion<?> criterionConditions) {
        this.criteria.put(name, criterionConditions);
        this.emptyCriterion = false;
        return this;
    }

    @Override
    public SimpleFurnitureRecipeJsonFactory group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public SimpleFurnitureRecipeJsonFactory showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getResult() {
        return this.output;
    }

    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.of(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        Advancement.Builder advancement$builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        ItemStack stack = new ItemStack(this.output, this.outputCount);
        stack.setTag(nbtElement);
        exporter.accept(recipeId, new SimpleFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group, stack, this.inputs), advancement$builder.build(recipeId.withPrefix("recipes/furniture/")));
    }

    private void validate(ResourceLocation recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}


