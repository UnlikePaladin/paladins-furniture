package com.unlikepaladin.pfm.recipes.forge;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.recipes.SimpleFurnitureRecipe;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerForge <J extends Recipe<I>, T extends RecipeSerializer<J>, I extends Container> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<J> {
    public FurnitureSerializerForge(T recipeSerializer) {
        this.serializer = recipeSerializer;
    }

    T serializer;
    @Override
    public J fromJson(ResourceLocation id, JsonObject json) {
        return serializer.fromJson(id, json);
    }

    @Nullable
    @Override
    public J fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return serializer.fromNetwork(id, buf);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, J recipe) {
        serializer.toNetwork(buf, recipe);
    }
}

