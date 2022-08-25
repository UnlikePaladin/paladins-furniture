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
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FurnitureRecipeSerializerForge extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<FurnitureRecipe> {
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

   /*@Override
    public Recipe read(Identifier id, PacketByteBuf buf) {
        return this.read(id, buf);
    }

    @Override
    public Recipe read(Identifier id, JsonObject json) {
        return this.read(id, json);
    }
}*/
}
