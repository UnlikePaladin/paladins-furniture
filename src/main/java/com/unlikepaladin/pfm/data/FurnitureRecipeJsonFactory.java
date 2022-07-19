package com.unlikepaladin.pfm.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.registry.RecipeRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FurnitureRecipeJsonFactory implements CraftingRecipeJsonFactory {
    private final Item output;
    private final int outputCount;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
    private final Advancement.Task builder = Advancement.Task.create();

    public static final Logger PFM_RECIPE_LOGGER = LogManager.getLogger();

    @Nullable
    private String group;

    public FurnitureRecipeJsonFactory(ItemConvertible output, int outputCount) {
        this.output = output.asItem();
        this.outputCount = outputCount;
    }

    public static FurnitureRecipeJsonFactory create(ItemConvertible output) {
        return FurnitureRecipeJsonFactory.create(output, 1);
    }

    public static FurnitureRecipeJsonFactory create(ItemConvertible output, int outputCount) {
        return new FurnitureRecipeJsonFactory(output, outputCount);
    }

    public FurnitureRecipeJsonFactory input(Character c, Tag<Item> tag) {
        return this.input(c, Ingredient.fromTag(tag));
    }

    public FurnitureRecipeJsonFactory input(Character c, ItemConvertible itemProvider) {
        return this.input(c, Ingredient.ofItems(itemProvider));
    }

    public FurnitureRecipeJsonFactory input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        }
        if (c.charValue() == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.inputs.put(c, ingredient);
        return this;
    }

    public FurnitureRecipeJsonFactory pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != this.pattern.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.pattern.add(patternStr);
        return this;
    }

    @Override
    public FurnitureRecipeJsonFactory criterion(String string, CriterionConditions criterionConditions) {
        this.builder.criterion(string, criterionConditions);
        return this;
    }

    @Override
    public FurnitureRecipeJsonFactory group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.builder.parent(new Identifier("recipes/root")).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
        exporter.accept(new FurnitureRecipeJsonProvider(recipeId, this.output, this.outputCount, this.group == null ? "" : this.group, this.pattern, this.inputs, this.builder, new Identifier(recipeId.getNamespace(), "recipes/" + this.output.getGroup().getName() + "/" + recipeId.getPath())));
    }

    private void validate(Identifier recipeId) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + recipeId + "!");
        }
        HashSet<Character> set = Sets.newHashSet(this.inputs.keySet());
        set.remove(Character.valueOf(' '));
        for (String string : this.pattern) {
            for (int i = 0; i < string.length(); ++i) {
                char c = string.charAt(i);
                if (!this.inputs.containsKey(Character.valueOf(c)) && c != ' ') {
                    throw new IllegalStateException("Pattern in recipe " + recipeId + " uses undefined symbol '" + c + "'");
                }
                set.remove(Character.valueOf(c));
            }
        }
        if (!set.isEmpty()) {
            throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + recipeId);
        }
        if (this.inputs.containsValue(Ingredient.EMPTY) || this.inputs.containsValue(Ingredient.ofItems(Items.AIR))) {
            PFM_RECIPE_LOGGER.warn("Recipe contains empty ingredient: " + recipeId);
        }
    }

    static class FurnitureRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier recipeId;
        private final Item output;
        private final int resultCount;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> inputs;
        private final Advancement.Task builder;
        private final Identifier advancementId;

        public FurnitureRecipeJsonProvider(Identifier recipeId, Item output, int resultCount, String group, List<String> pattern, Map<Character, Ingredient> inputs, Advancement.Task builder, Identifier advancementId) {
            this.recipeId = recipeId;
            this.output = output;
            this.resultCount = resultCount;
            this.group = group;
            this.pattern = pattern;
            this.inputs = inputs;
            this.builder = builder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            JsonArray jsonArray = new JsonArray();
            for (String string : this.pattern) {
                jsonArray.add(string);
            }
            json.add("pattern", jsonArray);
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<Character, Ingredient> entry : this.inputs.entrySet()) {
                jsonObject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
            }
            json.add("key", jsonObject);
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("item", Registry.ITEM.getId(this.output).toString());
            if (this.resultCount > 1) {
                jsonObject2.addProperty("count", this.resultCount);
            }
            json.add("result", jsonObject2);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return RecipeRegistry.FURNITURE_SERIALIZER;
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Override
        @Nullable
        public JsonObject toAdvancementJson() {
            return this.builder.toJson();
        }

        @Override
        @Nullable
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
