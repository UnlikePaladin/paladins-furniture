package com.unlikepaladin.pfm.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class FurnitureRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Item output;
    private final int outputCount;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.create();

    public static final Logger PFM_RECIPE_LOGGER = LogManager.getLogger();

    @Nullable
    private String group;

    private final RecipeCategory category;

    public FurnitureRecipeJsonBuilder(ItemConvertible output, int outputCount) {
        this.output = output.asItem();
        this.outputCount = outputCount;
        this.category = RecipeCategory.DECORATIONS;
    }

    public static FurnitureRecipeJsonBuilder create(ItemConvertible output) {
        return create(output, 1);
    }

    public static FurnitureRecipeJsonBuilder create(ItemConvertible output, int outputCount) {
        return new FurnitureRecipeJsonBuilder(output, outputCount);
    }

    public FurnitureRecipeJsonBuilder input(Character c, TagKey<Item> tag) {
        return this.input(c, Ingredient.fromTag(tag));
    }

    public FurnitureRecipeJsonBuilder input(Character c, ItemConvertible itemProvider) {
        return this.input(c, Ingredient.ofItems(new ItemConvertible[]{itemProvider}));
    }

    public FurnitureRecipeJsonBuilder input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.inputs.put(c, ingredient);
            return this;
        }
    }

    public FurnitureRecipeJsonBuilder pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != ((String)this.pattern.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternStr);
            return this;
        }
    }

    public FurnitureRecipeJsonBuilder criterion(String string, CriterionConditions criterionConditions) {
        this.advancementBuilder.criterion(string, criterionConditions);
        return this;
    }

    public FurnitureRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public Item getOutputItem() {
        return this.output;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.advancementBuilder.parent(new Identifier("recipes/root")).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(net.minecraft.advancement.AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
        Item output = this.output;
        int outputCount = this.outputCount;
        String group = this.group == null ? "" : this.group;
        List pattern = this.pattern;
        Map inputs = this.inputs;
        Advancement.Builder advancementBuilder = this.advancementBuilder;
        String recipeIdNamespace = recipeId.getNamespace();
        String name = this.category.getName();
        exporter.accept(new FurnitureRecipeJsonProvider(recipeId, output, outputCount, group, pattern, inputs, advancementBuilder, new Identifier(recipeIdNamespace, "recipes/" + name + "/" + recipeId.getPath())));
    }

    private void validate(Identifier recipeId) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for furniture recipe " + recipeId + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.inputs.keySet());
            set.remove(' ');
            Iterator patternIterator = this.pattern.iterator();

            while(patternIterator.hasNext()) {
                String string = (String)patternIterator.next();

                for(int i = 0; i < string.length(); ++i) {
                    char c = string.charAt(i);
                    if (!this.inputs.containsKey(c) && c != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + recipeId + " uses undefined symbol '" + c + "'");
                    }

                    set.remove(c);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + recipeId);
            }
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
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;

        public FurnitureRecipeJsonProvider(Identifier recipeId, Item output, int resultCount, String group, List<String> pattern, Map<Character, Ingredient> inputs, Advancement.Builder advancementBuilder, Identifier advancementId) {
            this.recipeId = recipeId;
            this.output = output;
            this.resultCount = resultCount;
            this.group = group;
            this.pattern = pattern;
            this.inputs = inputs;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();
            Iterator var3 = this.pattern.iterator();

            while(var3.hasNext()) {
                String string = (String)var3.next();
                jsonArray.add(string);
            }

            json.add("pattern", jsonArray);
            JsonObject jsonObject = new JsonObject();
            Iterator var7 = this.inputs.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<Character, Ingredient> entry = (Map.Entry)var7.next();
                jsonObject.add(String.valueOf(entry.getKey()), ((Ingredient)entry.getValue()).toJson());
            }

            json.add("key", jsonObject);
            JsonObject string = new JsonObject();
            string.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.resultCount > 1) {
                string.addProperty("count", this.resultCount);
            }

            json.add("result", string);
        }

        public RecipeSerializer<?> getSerializer() {
            return RecipeTypes.FURNITURE_SERIALIZER;
        }

        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Nullable
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}