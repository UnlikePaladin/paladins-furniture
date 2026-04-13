package com.unlikepaladin.pfm.runtime.data;


import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
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
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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

    public SimpleFurnitureRecipeJsonFactory input(RegistryEntryList<Item> tag) {
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
    public void save(RecipeOutput exporter, RegistryKey<Recipe<?>> recipeKey) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.ofItems(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        Advancement.Builder advancement$builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(advancement$builder::criterion);
        exporter.accept(recipeKey, new SimpleFurnitureRecipe(this.group == null || this.group.isBlank() ? "" : this.group, stack, this.inputs), advancement$builder.build(recipeKey.getValue().withPrefixedPath("recipes/furniture/")));
    }

    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.of(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        Recipe<?> recipe =  new SimpleFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group, stack, this.inputs);
        RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(RegistryKeys.RECIPE, recipeId);
        Advancement.Builder advancement$builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        exporter.accept(recipeKey, recipe, advancement$builder.build(recipeKey.getValue().withPrefix("recipes/furniture/")));
    }

        private void validate(ResourceLocation recipeId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }
}


