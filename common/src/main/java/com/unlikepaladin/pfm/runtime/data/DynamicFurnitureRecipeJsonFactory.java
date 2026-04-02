package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.recipes.DynamicFurnitureRecipe;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtTypes;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DynamicFurnitureRecipeJsonFactory {
    private final Advancement.Builder builder = Advancement.Builder.advancement();
    private List<Ingredient> vanillaIngredients = Lists.newArrayList();

    private final String outputClass;
    private final int outputCount;
    private String group;
    private Map<String, Integer> variantChildren = new HashMap<>();
    private final List<ResourceLocation> supportedVariants;
    @Nullable
    private final Tag nbtElement;
    private boolean emptyCriterion = true;

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.nbtElement = null;
        this.supportedVariants = supportedVariants;
        this.variantChildren = variantChildren;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, @Nullable Tag nbtElement) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.variantChildren = variantChildren;
        this.nbtElement = nbtElement;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.nbtElement = null;
        this.supportedVariants = supportedVariants;
        this.vanillaIngredients = inputs;
        this.variantChildren = variantChildren;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs, @Nullable Tag nbtElement) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.vanillaIngredients = inputs;
        this.variantChildren = variantChildren;
        this.nbtElement = nbtElement;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.nbtElement = null;
    }

    public DynamicFurnitureRecipeJsonFactory(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, @Nullable Tag nbtElement) {
        this.outputClass = output.getSimpleName();
        this.outputCount = outputCount;
        this.supportedVariants = supportedVariants;
        this.nbtElement = nbtElement;
    }



    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs, @Nullable Tag nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, inputs, nbtElement);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, List<Ingredient> inputs) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, inputs);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, @Nullable Tag nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, variantChildren, nbtElement);
    }
    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants);
    }

    public static DynamicFurnitureRecipeJsonFactory create(Class<? extends Block> output, int outputCount, List<ResourceLocation> supportedVariants, @Nullable Tag nbtElement) {
        return new DynamicFurnitureRecipeJsonFactory(output, outputCount, supportedVariants, nbtElement);
    }

    public DynamicFurnitureRecipeJsonFactory unlockedBy(String string, Criterion<?> criterionConditions) {
        this.builder.addCriterion(string, criterionConditions);
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

    public DynamicFurnitureRecipeJsonFactory vanillaInput(TagKey<Item> tag) {
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

    public void save(RecipeOutput exporter, Identifier recipeId) {
        if (emptyCriterion) {
            builder.addCriterion("has_workbench", PFMRecipeProvider.conditionsFromIngredient(Ingredient.ofItems(PaladinFurnitureModBlocksItems.WORKING_TABLE)));
        }
        exporter.accept(recipeId,
                new DynamicFurnitureRecipe(this.group == null || this.group.isBlank() ? " " : this.group,
                        new DynamicFurnitureRecipe.FurnitureOutput(outputClass, outputCount, nbtElement != null && nbtElement.getNbtType() == NbtCompound.TYPE ? (NbtCompound) nbtElement : new NbtCompound()), supportedVariants,
                        new DynamicFurnitureRecipe.FurnitureIngredients(vanillaIngredients, variantChildren)),
                builder.build(recipeId.withPrefixedPath("recipes/furniture/")));
    }


    public void save(RecipeOutput exporter) {
        this.save(exporter, new ResourceLocation(PaladinFurnitureMod.MOD_ID, this.getOutputClass().toLowerCase(Locale.US)));
    }

    public void save(RecipeOutput exporter, String recipePath) {
        ResourceLocation identifier2 = new ResourceLocation(recipePath);
        ResourceLocation identifier = new ResourceLocation(PaladinFurnitureMod.MOD_ID, this.getOutputClass().toLowerCase(Locale.US));
        if (identifier2.equals(identifier)) {
            throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
        }
        this.save(exporter, identifier2);
    }
}
