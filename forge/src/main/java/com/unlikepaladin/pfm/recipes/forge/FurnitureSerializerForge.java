package com.unlikepaladin.pfm.recipes.forge;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerForge implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializerForge() {
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

    public @Nullable FurnitureRecipe read(RegistryByteBuf buf) {
        return serializer.read(buf);
    }

    public void write(RegistryByteBuf buf, FurnitureRecipe recipe) {
        serializer.write(buf, recipe);
    }
}

