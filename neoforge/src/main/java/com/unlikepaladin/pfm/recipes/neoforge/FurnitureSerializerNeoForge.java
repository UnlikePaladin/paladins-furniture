package com.unlikepaladin.pfm.recipes.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.recipe.input.RecipeInput;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerNeoForge <J extends Recipe<I>, T extends RecipeSerializer<J>, I extends RecipeInput> implements RecipeSerializer<J> {
    public FurnitureSerializerNeoForge(T recipeSerializer) {
        this.serializer = recipeSerializer;
    }

    T serializer;
    @Override
    public MapCodec<J> codec() {
        return serializer.codec();
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, J> streamCodec() {
        return serializer.streamCodec();
    }
}

