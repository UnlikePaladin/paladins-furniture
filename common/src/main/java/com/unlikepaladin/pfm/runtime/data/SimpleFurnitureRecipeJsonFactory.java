package com.unlikepaladin.pfm.runtime.data;


import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleFurnitureRecipeJsonFactory implements RecipeBuilder {
    private final ItemStack stack;
    private final NonNullList<Ingredient> inputs = NonNullList.create();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private boolean showNotification = true;
    private boolean emptyCriterion = true;

    @Nullable
    private String group;

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount) {
        this.stack = new ItemStack(output, outputCount);
    }

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount, @NotNull DataComponentPatch components) {
        this.stack = new ItemStack(output, outputCount);
        this.stack.applyComponents(components);
    }

    public SimpleFurnitureRecipeJsonFactory(ItemStack stack) {
        this.stack = stack;
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count, DataComponentPatch components) {
        return new SimpleFurnitureRecipeJsonFactory(output, count, components);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, DataComponentPatch components) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1, components);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count) {
        return new SimpleFurnitureRecipeJsonFactory(output, count);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemStack stack) {
        return new SimpleFurnitureRecipeJsonFactory(stack);
    }

    public SimpleFurnitureRecipeJsonFactory input(HolderSet<Item> tag) {
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
        return this.stack.getItem();
    }

    @Override
    public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.of(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        Advancement.Builder advancement$builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        exporter.accept(recipeKey, new SimpleFurnitureRecipe(this.group == null || this.group.isBlank() ? "" : this.group, stack, this.inputs), advancement$builder.build(recipeKey.location().withPrefix("recipes/furniture/")));
    }

    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.of(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        Recipe<?> recipe =  new SimpleFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group, stack, this.inputs);
        ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, recipeId);
        Advancement.Builder advancement$builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        exporter.accept(recipeKey, recipe, advancement$builder.build(recipeKey.location().withPrefix("recipes/furniture/")));
    }

        private void validate(ResourceLocation recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}


