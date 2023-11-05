package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FurnitureRecipe implements Recipe<PlayerInventory>, Comparable<FurnitureRecipe> {
    final String group;
    protected final ItemStack output;
    final DefaultedList<Ingredient> input;

    public FurnitureRecipe(String group, ItemStack output, DefaultedList<Ingredient> input) {
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
    public ItemStack craft(PlayerInventory playerInventory, DynamicRegistryManager manager) {
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
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public String getGroup() {
        return this.group;
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
            Item item = Registries.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
            if (item == Items.AIR) {
                throw new JsonSyntaxException("Invalid item: " + string);
            }
            return item;
        }

        @Override
        public FurnitureRecipe read(PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItemStack();
            NbtCompound compound = packetByteBuf.readNbt();
            itemStack.setNbt(compound);
            return new FurnitureRecipe(string, itemStack, defaultedList);
        }

        @Override
        public Codec<FurnitureRecipe> codec() {
            return CODEC;
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, FurnitureRecipe furnitureRecipe) {
            packetByteBuf.writeString(furnitureRecipe.group);
            packetByteBuf.writeVarInt(furnitureRecipe.input.size());
            for (Ingredient ingredient : furnitureRecipe.input) {
                ingredient.write(packetByteBuf);
            }
            packetByteBuf.writeItemStack(furnitureRecipe.output);
            packetByteBuf.writeNbt(furnitureRecipe.output.getNbt());
        }

        private static final Codec<FurnitureRecipe> CODEC = RecordCodecBuilder.create((instance) ->
                instance.group(Codecs.createStrictOptionalFieldCodec(Codec.STRING, "group", "").forGetter(FurnitureRecipe::getGroup), FURNITURE_RESULT.fieldOf("result").forGetter((recipe) -> recipe.output), Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
            Ingredient[] ingredients2 = ingredients.stream().filter((ingredient) -> {
                return !ingredient.isEmpty();
            }).toArray(Ingredient[]::new);
            if (ingredients2.length == 0) {
                return DataResult.error(() -> {
                    return "No ingredients for furniture recipe";
                });
            } else {
                return DataResult.success(DefaultedList.copyOf(Ingredient.EMPTY, ingredients2));
            }
        }, DataResult::success).forGetter(FurnitureRecipe::getIngredients)).apply(instance, FurnitureRecipe::new));
    }

    private static final Codec<NbtCompound> NBT_CODEC = Codecs.xor(
            Codec.STRING, NbtCompound.CODEC
    ).flatXmap(either -> either.map(s -> {
        try {
            return DataResult.success(StringNbtReader.parse(s));
        } catch (CommandSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }, DataResult::success), nbtCompound -> DataResult.success(Either.left(nbtCompound.asString())));

    public static final Codec<NbtCompound> OUTPUT_TAGS = Codec.unboundedMap(Codec.STRING, NBT_CODEC).comapFlatMap(stringNbtCompoundMap -> {
        NbtCompound compound = new NbtCompound();
        stringNbtCompoundMap.forEach(compound::put);
        return DataResult.success(compound);
    }, nbtCompound -> {
        Map<String, NbtCompound> map = new HashMap<>();
        Set<String> keys = nbtCompound.getKeys();
        keys.forEach(s -> {
            NbtCompound compound = new NbtCompound();
            if (nbtCompound.get(s) instanceof NbtCompound) {
                compound = nbtCompound.getCompound(s);
            } else {
                compound.put(s, nbtCompound.get(s));
            }
            map.put(s, compound);
        });
        return map;
    });

    private static final Codec<Item> CRAFTING_RESULT_ITEM = Codecs.validate(Registries.ITEM.getCodec(), (item) -> {
        return item == Items.AIR ? DataResult.error(() -> {
            return "Crafting result must not be minecraft:air";
        }) : DataResult.success(item);
    });
    public static final Codec<ItemStack> FURNITURE_RESULT = RecordCodecBuilder.create((instance) -> {
        return instance.group(CRAFTING_RESULT_ITEM.fieldOf("item").forGetter(ItemStack::getItem), Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount), Codecs.createStrictOptionalFieldCodec(OUTPUT_TAGS, "tag", new NbtCompound()).forGetter(ItemStack::getNbt)).apply(instance, (item, integer, nbtElement) -> {
            ItemStack stack = new ItemStack(item, integer);
            stack.setNbt(nbtElement);
            return stack;
        });
    });
}
