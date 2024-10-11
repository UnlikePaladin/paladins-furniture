package com.unlikepaladin.pfm.recipes.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerNeoForge implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializerNeoForge() {
        serializer = new FurnitureRecipe.Serializer();
    }
    FurnitureRecipe.Serializer serializer;

    @Override
    public MapCodec<FurnitureRecipe> codec() {
        return serializer.codec();
    }

    @Override
    public PacketCodec<RegistryByteBuf, FurnitureRecipe> packetCodec() {
        return serializer.packetCodec();
    }
}

