package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DynamicFurnitureRecipeJsonFactory {
    private final Advancement.Builder builder = Advancement.Builder.advancement();
    private List<Ingredient> vanillaIngredients = Lists.newArrayList();

    private final String outputClass;
    private final int outputCount;
    private String group;
    private Map<String, Integer> variantChildren = new HashMap<>();
    private final List<ResourceLocation> supportedVariants;
    private final DataComponentPatch components;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private boolean emptyCriterion = true;

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.components = DataComponentPatch.EMPTY;
        this.supportedVariants = supportedVariants;
        this.variantChildren = variantChildren;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, @Nullable DataComponentPatch components) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.variantChildren = variantChildren;
        this.components = components;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.components = DataComponentPatch.EMPTY;
        this.supportedVariants = supportedVariants;
        this.vanillaIngredients = inputs;
        this.variantChildren = variantChildren;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs, DataComponentPatch components) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.vanillaIngredients = inputs;
        this.variantChildren = variantChildren;
        this.components = components;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.components = DataComponentPatch.EMPTY;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, DataComponentPatch components) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.components = components;
    }



    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs, DataComponentPatch nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, inputs, nbtElement);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, inputs);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, DataComponentPatch nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, nbtElement);
    }
    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, DataComponentPatch nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, nbtElement);
    }

    public DynamicFurnitureRecipeJsonFactory criterion(String name, Criterion<?> criterionConditions) {
        this.criteria.put(name, criterionConditions);
        this.emptyCriterion = false;
        return this;
    }


    public DynamicFurnitureRecipeJsonFactory group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public String getOutputClass() {
        return outputClass;
    }

    public DynamicFurnitureRecipeJsonFactory vanillaInput(RegistryEntryList<Item> tag) {
        return this.vanillaInput(Ingredient.of(tag));
    }

    public DynamicFurnitureRecipeJsonFactory vanillaInput(Ingredient ingredient) {
        return this.vanillaInput(ingredient, 1);
    }

    public DynamicFurnitureRecipeJsonFactory vanillaInput(ItemLike itemProvider) {
        return this.vanillaInput(itemProvider, 1);
    }

    public DynamicFurnitureRecipeJsonFactory vanillaInput(ItemLike itemProvider, int size) {
        for (int i = 0; i < size; ++i) {
            this.vanillaInput(Ingredient.of(itemProvider));
        }
        return this;
    }

    public DynamicFurnitureRecipeJsonFactory vanillaInput(Ingredient ingredient, int size) {
        for (int i = 0; i < size; ++i) {
            this.vanillaIngredients.add(ingredient);
        }
        return this;
    }

    public DynamicFurnitureRecipeJsonFactory childInput(String ingredient, int size) {
        variantChildren.put(ingredient, size);
        return this;
    }
    public DynamicFurnitureRecipeJsonFactory childInput(String ingredient) {
        return childInput(ingredient, 1);
    }

    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.ofItems(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }

        Advancement.Builder advancement$builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(advancement$builder::criterion);

        exporter.accept(recipeKey,
                new DynamicFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group,
                        new DynamicFurnitureRecipe.FurnitureOutput(outputClass, outputCount, components), supportedVariants,
                        new DynamicFurnitureRecipe.FurnitureIngredients(vanillaIngredients, variantChildren)),
                advancement$builder.build(recipeKey.getValue().withPrefixedPath("recipes/furniture/")));
    }

    public void save(RecipeOutput exporter, ResourceLocation recipeId) {
        if (emptyCriterion) {
            criteria.put("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.of(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(RegistryKeys.RECIPE, recipeId);
        Advancement.Builder advancement$builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancement$builder::addCriterion);

        exporter.accept(recipeKey,
                new DynamicFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group,
                        new DynamicFurnitureRecipe.FurnitureOutput(outputClass, outputCount, components), supportedVariants,
                        new DynamicFurnitureRecipe.FurnitureIngredients(vanillaIngredients, variantChildren)),
                advancement$builder.build(recipeKey.getValue().withPrefixedPath("recipes/furniture/")));
    }
}
