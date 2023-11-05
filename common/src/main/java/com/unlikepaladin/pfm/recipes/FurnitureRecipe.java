package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FurnitureRecipe implements Recipe<PlayerInventory>, Comparable<FurnitureRecipe> {
    private final Identifier id;
    final String group;
    final ItemStack output;
    final DefaultedList<Ingredient> input;

    public FurnitureRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        this.id = id;
        this.group = group;
        this.output = output;
        this.input = input;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean matches(PlayerInventory playerInventory, World world) {
        List<Ingredient> ingredients = this.getIngredients();
        BitSet hasIngredients = new BitSet(ingredients.size());
        HashMap<Item, Integer> containedItems = new HashMap<>();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            for (ItemStack stack : ingredient.getMatchingStacks()) {
                int itemCount = 0;
                for (ItemStack stack1 : playerInventory.main) {
                    if (stack.isOf(stack1.getItem())) {
                        itemCount += stack1.getCount();
                    }
                }
                if (itemCount == 0)
                    break;

                if (getSlotWithStackIgnoreNBT(playerInventory, stack) != -1){
                    if (!containedItems.containsKey(stack.getItem())) {
                        if (itemCount >= stack.getCount()) {
                            hasIngredients.set(i, true);
                            containedItems.put(stack.getItem(), 1);
                        }
                    } else {
                        if (itemCount >= (containedItems.get(stack.getItem()) + 1)) {
                            hasIngredients.set(i, true);
                            containedItems.put(stack.getItem(), containedItems.get(stack.getItem()) + 1);
                        }
                    }
                }
            }
        }
        boolean matches = true;
        for (int i = 0; i < ingredients.size(); i++){
            if (!hasIngredients.get(i)) {
                matches = false;
                break;
            }
        }
        return matches;
    }

    public static int getSlotWithStackIgnoreNBT(PlayerInventory inventory, ItemStack stack) {
        for(int i = 0; i < inventory.main.size(); ++i) {
            if (!inventory.main.get(i).isEmpty() && stack.isOf(inventory.main.get(i).getItem())) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public ItemStack craft(PlayerInventory playerInventory) {
        if (this.output.getNbt() != null && this.output.getNbt().isEmpty()) {
            ItemStack stack = this.output.copy();
            stack.setNbt(null);
            return stack;
        }
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public ItemStack createIcon() {
        return PaladinFurnitureModBlocksItems.WORKING_TABLE.asItem().getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.FURNITURE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.FURNITURE_RECIPE;
    }

    @Override
    public int compareTo(@NotNull FurnitureRecipe furnitureRecipe) {
        return this.output.toString().compareTo(furnitureRecipe.output.toString());
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    public static class Serializer
            implements RecipeSerializer<FurnitureRecipe> {
        @Override
        public FurnitureRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for furniture recipe");
            }
            ItemStack itemStack = outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new FurnitureRecipe(identifier, string, itemStack, defaultedList);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        public static ItemStack outputFromJson(JsonObject json) {
            Item item = getItem(json);
            Map<String, NbtElement> elementList = null;
            if (json.has("tag")) {
                elementList = new HashMap<>();
                for(Map.Entry<String, JsonElement> jsonObject : json.get("tag").getAsJsonObject().entrySet()) {
                    elementList.put(jsonObject.getKey(), JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonObject.getValue()));
                }
            }
            int i = JsonHelper.getInt(json, "count", 1);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid output count: " + i);
            }
            ItemStack stack = new ItemStack(item, i);
            NbtCompound compound = new NbtCompound();
            if (elementList != null) {
                for(Map.Entry<String, NbtElement> nbtElementEntry : elementList.entrySet()) {
                    compound.put(nbtElementEntry.getKey(), nbtElementEntry.getValue());
                }
            }
            stack.setNbt(compound);
            return stack;
        }

        public static Item getItem(JsonObject json) {
            String string = JsonHelper.getString(json, "item");
            Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Invalid item: " + string);
            }
            return item;
        }

        @Override
        public FurnitureRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItemStack();
            return new FurnitureRecipe(identifier, string, itemStack, defaultedList);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, FurnitureRecipe furnitureRecipe) {
            packetByteBuf.writeString(furnitureRecipe.group);
            packetByteBuf.writeVarInt(furnitureRecipe.input.size());
            for (Ingredient ingredient : furnitureRecipe.input) {
                ingredient.write(packetByteBuf);
            }
            packetByteBuf.writeItemStack(furnitureRecipe.output);
        }
    }
}
