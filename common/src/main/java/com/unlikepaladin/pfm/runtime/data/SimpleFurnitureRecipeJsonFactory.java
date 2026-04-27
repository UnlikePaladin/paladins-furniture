package com.unlikepaladin.pfm.runtime.data;


import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SimpleFurnitureRecipeJsonFactory {
    private final Item output;
    private final int outputCount;
    private final List<Ingredient> inputs = Lists.newArrayList();
    private final Advancement.Builder builder = Advancement.Builder.advancement();

    @Nullable
    private Tag nbtElement;
    @Nullable
    private String group;

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount) {
        this.output = output.asItem();
        this.outputCount = outputCount;
    }

    public SimpleFurnitureRecipeJsonFactory(ItemLike output, int outputCount, @Nullable Tag nbtElement) {
        this.output = output.asItem();
        this.outputCount = outputCount;
        this.nbtElement = nbtElement;
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count, Tag nbtElement) {
        return new SimpleFurnitureRecipeJsonFactory(output, count, nbtElement);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, Tag nbtElement) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1, nbtElement);
    }

    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output) {
        return new SimpleFurnitureRecipeJsonFactory(output, 1);
    }
    public static SimpleFurnitureRecipeJsonFactory create(ItemLike output, int count) {
        return new SimpleFurnitureRecipeJsonFactory(output, count);
    }
    public SimpleFurnitureRecipeJsonFactory input(net.minecraft.tags.Tag tag) {
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

    public SimpleFurnitureRecipeJsonFactory unlockedBy(String string, CriterionTriggerInstance criterionConditions) {
        this.builder.addCriterion(string, criterionConditions);
        return this;
    }

    public SimpleFurnitureRecipeJsonFactory group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public Item getResult() {
        return this.output;
    }

    public void save(Consumer<FinishedRecipe> exporter, ResourceLocation recipeId) {
        this.builder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        exporter.accept(new SimpleFurnitureRecipeJsonProvider(recipeId, this.output, this.nbtElement, this.outputCount, this.group == null ? "" : this.group, this.inputs, this.builder, new ResourceLocation(recipeId.getNamespace(), "recipes/" + this.output.getItemCategory().getRecipeFolderName() + "/" + recipeId.getPath())));
    }

    private void validate(ResourceLocation recipeId) {
        if (this.builder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }

    public static class SimpleFurnitureRecipeJsonProvider
            implements FinishedRecipe {
        private final ResourceLocation recipeId;
        private final Item output;
        private final int count;
        private final String group;
        private final List<Ingredient> inputs;
        private final Advancement.Builder builder;
        private final ResourceLocation advancementId;
        @Nullable
        private final Tag nbtElement;

        public SimpleFurnitureRecipeJsonProvider(ResourceLocation recipeId, Item output, @Nullable Tag nbtElement, int outputCount, String group, List<Ingredient> inputs, Advancement.Builder builder, ResourceLocation advancementId) {
            this.recipeId = recipeId;
            this.output = output;
            this.count = outputCount;
            this.group = group;
            this.inputs = inputs;
            this.builder = builder;
            this.advancementId = advancementId;
            this.nbtElement = nbtElement;
        } 

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            JsonArray jsonArray = new JsonArray();
            for (Ingredient ingredient : this.inputs) {
                jsonArray.add(ingredient.toJson());
            }
            json.add("ingredients", jsonArray);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registry.ITEM.getKey(this.output).toString());
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
            return RecipeTypes.SIMPLE_FURNITURE_SERIALIZER;
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


