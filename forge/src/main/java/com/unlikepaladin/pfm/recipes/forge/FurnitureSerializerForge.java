package com.unlikepaladin.pfm.recipes.forge;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerForge <J extends Recipe<I>, T extends RecipeSerializer<J>, I extends Container> implements RecipeSerializer<J> {
    public FurnitureSerializerForge(T recipeSerializer) {
        this.serializer = recipeSerializer;
    }

    T serializer;
    @Override
    public Codec<J> codec() {
        return serializer.codec();
    }

    @Override
    public @Nullable J fromNetwork(FriendlyByteBuf buf) {
        return serializer.fromNetwork(buf);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, J recipe) {
        serializer.toNetwork(buf, recipe);
    }
}

