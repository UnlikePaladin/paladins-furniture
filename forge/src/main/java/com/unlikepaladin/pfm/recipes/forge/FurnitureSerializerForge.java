package com.unlikepaladin.pfm.recipes.forge;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class FurnitureSerializerForge extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializerForge() {
        serializer = new FurnitureRecipe.Serializer();
    }
    FurnitureRecipe.Serializer serializer;
    @Override
    public FurnitureRecipe read(Identifier id, JsonObject json) {
        return serializer.read(id, json);
    }

    @Nullable
    @Override
    public FurnitureRecipe read(Identifier id, PacketByteBuf buf) {
        return serializer.read(id, buf);
    }

    @Override
    public void write(PacketByteBuf buf, FurnitureRecipe recipe) {
        serializer.write(buf, recipe);
    }
}

