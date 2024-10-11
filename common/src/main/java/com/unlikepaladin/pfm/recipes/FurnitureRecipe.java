package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
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
    public ItemStack craft(PlayerInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        if (!this.output.getComponents().isEmpty() && output.contains(DataComponentTypes.BLOCK_ENTITY_DATA) && output.get(DataComponentTypes.BLOCK_ENTITY_DATA).isEmpty()) {
            ItemStack stack = this.output.copy();
            stack.remove(DataComponentTypes.BLOCK_ENTITY_DATA);
            return stack;
        }
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.output.copy();
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

        public static FurnitureRecipe read(RegistryByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.PACKET_CODEC.decode(packetByteBuf));
            }
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(packetByteBuf);
            return new FurnitureRecipe(string, itemStack, defaultedList);
        }

        public static final PacketCodec<RegistryByteBuf, FurnitureRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                FurnitureRecipe.Serializer::write, FurnitureRecipe.Serializer::read
        );
        @Override
        public MapCodec<FurnitureRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FurnitureRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static void write(RegistryByteBuf packetByteBuf, FurnitureRecipe furnitureRecipe) {
            packetByteBuf.writeString(furnitureRecipe.group);
            packetByteBuf.writeVarInt(furnitureRecipe.input.size());
            for (Ingredient ingredient : furnitureRecipe.input) {
                Ingredient.PACKET_CODEC.encode(packetByteBuf, ingredient);
            }
            ItemStack.PACKET_CODEC.encode(packetByteBuf, furnitureRecipe.output);
        }

        private static final MapCodec<FurnitureRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(FurnitureRecipe::getGroup),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                        Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
            Ingredient[] ingredients2 = ingredients.stream().filter((ingredient) -> {
                return !ingredient.isEmpty();
            }).toArray(Ingredient[]::new);
            if (ingredients2.length == 0) {
                return DataResult.error(() -> "No ingredients for furniture recipe");
            } else {
                return DataResult.success(DefaultedList.copyOf(Ingredient.EMPTY, ingredients2));
            }
        }, DataResult::success).forGetter(FurnitureRecipe::getIngredients))
                        .apply(instance, FurnitureRecipe::new));
    }

    private static final Codec<NbtCompound> NBT_CODEC = Codec.xor(
            Codec.STRING, NbtCompound.CODEC
    ).flatXmap(either -> either.map(s -> {
        try {
            return DataResult.success(StringNbtReader.parse(s));
        } catch (CommandSyntaxException e) {
            return DataResult.success(new NbtCompound());
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

    /*
    private static final Codec<Item> CRAFTING_RESULT_ITEM = Registries.ITEM.getCodec().validate((item) -> {
        return item == Items.AIR ? DataResult.error(() -> {
            return "Crafting result must not be minecraft:air";
        }) : DataResult.success(item);
    });
    public static final Codec<ItemStack> FURNITURE_RESULT = RecordCodecBuilder.create((instance) ->
            instance.group(
            CRAFTING_RESULT_ITEM.fieldOf("item").forGetter(ItemStack::getItem),
            Codecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount),
            OUTPUT_TAGS.optionalFieldOf( "tag", new NbtCompound())
                    .xmap(NbtComponent::of, NbtComponent::copyNbt)
                    .xmap(nbtComponent -> ComponentMap.builder().add(DataComponentTypes.BLOCK_ENTITY_DATA, nbtComponent).build(), components -> components.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT))
                    .forGetter(ItemStack::getComponents)).apply(instance, (item, integer, components) -> {
                        ItemStack stack = new ItemStack(item, integer);
                        stack.applyComponentsFrom(components);
                        return stack;
    }));*/
}
