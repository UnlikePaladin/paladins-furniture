package com.unlikepaladin.pfm.runtime.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

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

    public DynamicFurnitureRecipeJsonFactory unlockedBy(String string, CriterionTriggerInstance criterionConditions) {
        this.builder.addCriterion(string, criterionConditions);
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

    public void save(Consumer<FinishedRecipe> exporter, ResourceLocation recipeId) {
        this.builder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        exporter.accept(new DynamicFurnitureRecipeJsonProvider(recipeId, outputClass, nbtElement, outputCount, group, vanillaIngredients, supportedVariants, variantChildren, builder, new ResourceLocation(recipeId.getNamespace(), "recipes/furniture/" + recipeId.getPath())));
    }


    public void save(Consumer<FinishedRecipe> exporter) {
        this.save(exporter, new ResourceLocation(PaladinFurnitureMod.MOD_ID, this.getOutputClass().toLowerCase(Locale.US)));
    }

    public void save(Consumer<FinishedRecipe> exporter, String recipePath) {
        ResourceLocation identifier2 = new ResourceLocation(recipePath);
        ResourceLocation identifier = new ResourceLocation(PaladinFurnitureMod.MOD_ID, this.getOutputClass().toLowerCase(Locale.US));
        if (identifier2.equals(identifier)) {
            throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
        }
        this.save(exporter, identifier2);
    }


    public static class DynamicFurnitureRecipeJsonProvider
            implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final String outputClass;
        private final int count;
        private final String group;
        private final List<Ingredient> vanillaIngredients;
        private final Map<String, Integer> variantChildren;
        private final List<ResourceLocation> supportedVariants;
        private final Advancement.Builder builder;
        private final ResourceLocation advancementId;
        @Nullable
        private final Tag nbtElement;

        public DynamicFurnitureRecipeJsonProvider(ResourceLocation recipeId, String outputClass, @Nullable Tag nbtElement, int outputCount, String group, List<Ingredient> vanillaIngredients, List<ResourceLocation> supportedVariants, Map<String, Integer> variantChildren, Advancement.Builder builder, ResourceLocation advancementId) {
            this.recipeId = recipeId;
            this.outputClass = outputClass;
            this.count = outputCount;
            this.group = group;
            this.vanillaIngredients = vanillaIngredients;
            this.builder = builder;
            this.advancementId = advancementId;
            this.nbtElement = nbtElement;
            this.variantChildren = variantChildren;
            this.supportedVariants = supportedVariants;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.group != null && !this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            JsonArray identifierArray = new JsonArray();
            for (ResourceLocation identifier : this.supportedVariants) {
                identifierArray.add(identifier.toString());
            }
            json.add("supportedVariants", identifierArray);

            JsonObject ingredients = new JsonObject();

            JsonArray ingredientArray = new JsonArray();
            for (Ingredient ingredient : this.vanillaIngredients) {
                ingredientArray.add(ingredient.toJson());
            }
            ingredients.add("vanillaIngredients", ingredientArray);

            JsonObject variantChildrenObject = new JsonObject();
            for (Map.Entry<String, Integer> entry : this.variantChildren.entrySet()) {
                variantChildrenObject.addProperty(entry.getKey(), entry.getValue());
            }
            ingredients.add("variantChildren", variantChildrenObject);

            json.add("ingredients", ingredients);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("outputClass", outputClass);
            if (this.count > 1) {
                jsonObject.addProperty("count", this.count);
            }
            if (nbtElement != null) {
                JsonElement object = NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, this.nbtElement);
                jsonObject.add("tag", object);
            }
            json.add("result", jsonObject);

        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeTypes.DYNAMIC_FURNITURE_SERIALIZER;
        }

        @Override
        public ResourceLocation getId() {
            return this.recipeId;
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.builder.serializeToJson();
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
