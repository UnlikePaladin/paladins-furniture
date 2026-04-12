package com.unlikepaladin.pfm.recipes.forge;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FurnitureSerializerForge <J extends Recipe<I>, T extends RecipeSerializer<J>, I extends RecipeInput> implements RecipeSerializer<J> {
    public FurnitureSerializerForge(T recipeSerializer) {
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

