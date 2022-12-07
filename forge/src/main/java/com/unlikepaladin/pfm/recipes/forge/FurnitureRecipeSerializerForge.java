package com.unlikepaladin.pfm.recipes.forge;

import com.google.gson.JsonObject;
import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import com.unlikepaladin.pfm.recipes.FurnitureSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class FurnitureRecipeSerializerForge implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureRecipeSerializerForge() {
        serializer = new FurnitureSerializer();
    }
    FurnitureSerializer serializer;
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
