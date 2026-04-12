package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.*;

public class SimpleFurnitureRecipe implements FurnitureRecipe, FurnitureRecipe.CraftableFurnitureRecipe {
    final String group;
    final ItemStack output;
    final NonNullList<Ingredient> input;


    public SimpleFurnitureRecipe(String group, ItemStack output, List<Ingredient> input) {
        this.group = group;
        this.output = output;
        this.input = NonNullList.of(Ingredient.EMPTY, input.toArray(Ingredient[]::new));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean matches(FurnitureRecipe.FurnitureRecipeInput input, Level world) {
        List<Ingredient> ingredients = this.getIngredients();
        BitSet hasIngredients = new BitSet(ingredients.size());
        PlayerInventory playerInventory = input.playerInventory();
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
    public ItemStack assemble(FurnitureRecipe.FurnitureRecipeInput playerInventory, HolderLookup.Provider registryManager) {
        if (!this.output.getComponents().isEmpty() && output.has(DataComponents.BLOCK_ENTITY_DATA) && output.get(DataComponents.BLOCK_ENTITY_DATA).isEmpty()) {
            ItemStack stack = this.output.copy();
            stack.remove(DataComponents.BLOCK_ENTITY_DATA);
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
    public ItemStack getResultItem(HolderLookup.Provider registryManager) {
        return this.output;
    }

    @Override
    public ItemStack getToastSymbol() {
        return PaladinFurnitureModBlocksItems.WORKING_TABLE.asItem().getDefaultInstance();
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

        private static final MapCodec<SimpleFurnitureRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                                Codec.STRING.optionalFieldOf("group", "").forGetter(SimpleFurnitureRecipe::getGroup),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap((ingredients) -> {
                                    Ingredient[] ingredients2 = ingredients.stream().filter((ingredient) -> {
                                        return !ingredient.isEmpty();
                                    }).toArray(Ingredient[]::new);
                                    if (ingredients2.length == 0) {
                                        return DataResult.error(() -> "No ingredients for furniture recipe");
                                    } else {
                                        return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients2));
                                    }
                                }, DataResult::success).forGetter(SimpleFurnitureRecipe::getIngredients))
                        .apply(instance, SimpleFurnitureRecipe::new));

        @Override
        public MapCodec<SimpleFurnitureRecipe> codec() {
            return CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, SimpleFurnitureRecipe> PACKET_CODEC = StreamCodec.of(
                SimpleFurnitureRecipe.Serializer::write, SimpleFurnitureRecipe.Serializer::read
        );
        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SimpleFurnitureRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        public static SimpleFurnitureRecipe read(RegistryFriendlyByteBuf packetByteBuf) {
            String string = packetByteBuf.readUtf();
            int i = packetByteBuf.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(packetByteBuf));
            }
            ItemStack itemStack = ItemStack.STREAM_CODEC.decode(packetByteBuf);
            return new SimpleFurnitureRecipe(string, itemStack, defaultedList);
        }

        public static void write(RegistryFriendlyByteBuf packetByteBuf, SimpleFurnitureRecipe simpleFurnitureRecipe) {
            packetByteBuf.writeUtf(simpleFurnitureRecipe.group);
            packetByteBuf.writeVarInt(simpleFurnitureRecipe.input.size());
            for (Ingredient ingredient : simpleFurnitureRecipe.input) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(packetByteBuf, ingredient);
            }
            ItemStack.STREAM_CODEC.encode(packetByteBuf, simpleFurnitureRecipe.output);
        }
    }
}
