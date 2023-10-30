package com.unlikepaladin.pfm.recipes.forge;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerForge implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializerForge() {
        serializer = new FurnitureRecipe.Serializer();
    }
    FurnitureRecipe.Serializer serializer;
    @Override
    public Codec<FurnitureRecipe> codec() {
        return serializer.codec();
    }

    @Override
    public @Nullable FurnitureRecipe read(PacketByteBuf buf) {
        return serializer.read(buf);
    }

    @Override
    public void write(PacketByteBuf buf, FurnitureRecipe recipe) {
        serializer.write(buf, recipe);
    }
}

