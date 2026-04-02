package com.unlikepaladin.pfm.recipes;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.level.Level;

import java.util.*;

public class SimpleFurnitureRecipe implements FurnitureRecipe, FurnitureRecipe.CraftableFurnitureRecipe {
    final String group;
    final ItemStack output;
    final NonNullList<Ingredient> input;

    public SimpleFurnitureRecipe(String group, ItemStack output, List<Ingredient> input) {
        this.group = group;
        this.output = output;
        this.input = DefaultedList.copyOf(Ingredient.EMPTY, input.toArray(Ingredient[]::new));
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
    public ItemStack getToastSymbol() {
        return PaladinFurnitureModBlocksItems.WORKING_TABLE.asItem().getDefaultStack();
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

        Codec<SimpleFurnitureRecipe> CODEC = RecordCodecBuilder.create(simpleFurnitureRecipeInstance ->
                simpleFurnitureRecipeInstance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(SimpleFurnitureRecipe::getGroup),
                        FURNITURE_RESULT.fieldOf("result").forGetter(recipe -> recipe.output),
                        Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(SimpleFurnitureRecipe::getIngredients))
                        .apply(simpleFurnitureRecipeInstance, SimpleFurnitureRecipe::new)
        );

        private static final Codec<Item> CRAFTING_RESULT_ITEM = Codecs.validate(Registries.ITEM.getCodec(), (item) -> {
            return item == Items.AIR ? DataResult.error(() -> {
                return "Crafting result must not be minecraft:air";
            }) : DataResult.success(item);
        });

        public static final Codec<ItemStack> FURNITURE_RESULT = RecordCodecBuilder.create((instance) -> {
            return instance.group(CRAFTING_RESULT_ITEM.fieldOf("item").forGetter(ItemStack::getItem), Codecs.createStrictOptionalFieldCodec(Codecs.POSITIVE_INT, "count", 1)
                            .forGetter(ItemStack::getCount), Codecs.createStrictOptionalFieldCodec(NbtCompound.CODEC, "tag", new NbtCompound()).forGetter(ItemStack::getNbt))
                    .apply(instance, (item, integer, nbtElement) -> {
                ItemStack stack = new ItemStack(item, integer);
                stack.setNbt(nbtElement);
                return stack;
            });
        });

        @Override
        public Codec<SimpleFurnitureRecipe> codec() {
            return CODEC;
        }

        @Override
        public SimpleFurnitureRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int i = packetByteBuf.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromNetwork(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItem();
            return new SimpleFurnitureRecipe(string, itemStack, defaultedList);
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
