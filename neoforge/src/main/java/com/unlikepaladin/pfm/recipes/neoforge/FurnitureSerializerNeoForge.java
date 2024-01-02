package com.unlikepaladin.pfm.recipes.neoforge;

import com.mojang.serialization.Codec;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerNeoForge implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializerNeoForge() {
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

