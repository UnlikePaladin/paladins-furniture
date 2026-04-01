package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.*;

public class SimpleFurnitureRecipe implements FurnitureRecipe, FurnitureRecipe.CraftableFurnitureRecipe {
    private final ResourceLocation id;
    final String group;
    final ItemStack output;
    final NonNullList<Ingredient> input;

    public SimpleFurnitureRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> input) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory playerInventory, Level world) {
        List<Ingredient> ingredients = this.getIngredients();
        BitSet hasIngredients = new BitSet(ingredients.size());

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            for (ItemStack stack : ingredient.getItems()) {
                if (playerInventory.countItem(stack.getItem()) >= stack.getCount()) {
                    hasIngredients.set(i);
                    break;
                }
            }
        }
        return hasIngredients.cardinality() == ingredients.size();
    }

    @Override
    public FurnitureRecipe parent() {
        return this;
    }

    @Override
    public ItemStack getRecipeOuput() {
        return output;
    }


    @Override
    public ItemStack assemble(Inventory playerInventory, RegistryAccess registryManager) {
        if (this.output.getTag() != null && this.output.getTag().isEmpty()) {
            ItemStack stack = this.output.copy();
            stack.setTag(null);
            return stack;
        }
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.SIMPLE_FURNITURE_SERIALIZER;
    }

    @Override
    public String outputClass() {
        return this.output.getItem().getClass().getSimpleName();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public List<CraftableFurnitureRecipe> getInnerRecipes() {
        return Collections.singletonList(this);
    }

    public static class Serializer
            implements RecipeSerializer<SimpleFurnitureRecipe> {
        @Override
        public SimpleFurnitureRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            NonNullList<Ingredient> defaultedList = getIngredients(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for furniture recipe");
            }
            ItemStack itemStack = outputFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new SimpleFurnitureRecipe(identifier, string, itemStack, defaultedList);
        }

        private static NonNullList<Ingredient> getIngredients(JsonArray json) {
            NonNullList<Ingredient> defaultedList = NonNullList.create();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        public static ItemStack outputFromJson(JsonObject json) {
            Item item = getItem(json);
            Map<String, Tag> elementList = null;
            if (json.has("tag")) {
                elementList = new HashMap<>();
                for(Map.Entry<String, JsonElement> jsonObject : json.get("tag").getAsJsonObject().entrySet()) {
                    elementList.put(jsonObject.getKey(), JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonObject.getValue()));
                }
            }
            int i = GsonHelper.getAsInt(json, "count", 1);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid output count: " + i);
            }
            ItemStack stack = new ItemStack(item, i);
            CompoundTag compound = new CompoundTag();
            if (elementList != null) {
                for(Map.Entry<String, Tag> nbtElementEntry : elementList.entrySet()) {
                    compound.put(nbtElementEntry.getKey(), nbtElementEntry.getValue());
                }
            }
            if (!compound.isEmpty())
                stack.setTag(compound);
            return stack;
        }

        public static Item getItem(JsonObject json) {
            String string = GsonHelper.getAsString(json, "item");
            Item item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Invalid item: " + string);
            }
            return item;
        }

        @Override
        public SimpleFurnitureRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
            String string = packetByteBuf.readUtf();
            int i = packetByteBuf.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromNetwork(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItem();
            return new SimpleFurnitureRecipe(identifier, string, itemStack, defaultedList);
        }

        @Override
        public void toNetwork(FriendlyByteBuf packetByteBuf, SimpleFurnitureRecipe simpleFurnitureRecipe) {
            packetByteBuf.writeUtf(simpleFurnitureRecipe.group);
            packetByteBuf.writeVarInt(simpleFurnitureRecipe.input.size());
            for (Ingredient ingredient : simpleFurnitureRecipe.input) {
                ingredient.toNetwork(packetByteBuf);
            }
            packetByteBuf.writeItem(simpleFurnitureRecipe.output);
        }
    }
}
