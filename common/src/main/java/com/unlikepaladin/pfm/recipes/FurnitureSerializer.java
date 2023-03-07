package com.unlikepaladin.pfm.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class FurnitureSerializer implements RecipeSerializer<FurnitureRecipe> {
    public FurnitureSerializer() {
    }

    @Override
    public FurnitureRecipe read(Identifier identifier, JsonObject jsonObject) {
        String string = JsonHelper.getString(jsonObject, "group", "");
        Map<String, Ingredient> map = FurnitureRecipe.readSymbols(JsonHelper.getObject(jsonObject, "key"));
        String[] strings = FurnitureRecipe.removePadding(FurnitureRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
        int i = strings[0].length();
        int j = strings.length;
        DefaultedList<Ingredient> defaultedList = FurnitureRecipe.createPatternMatrix(strings, map, i, j);
        ItemStack itemStack = FurnitureRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
        return new FurnitureRecipe(identifier, string, i, j, defaultedList, itemStack);
    }

    @Override
    public FurnitureRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
        int i = packetByteBuf.readVarInt();
        int j = packetByteBuf.readVarInt();
        String string = packetByteBuf.readString();
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
        for (int k = 0; k < defaultedList.size(); ++k) {
            defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
        }
        ItemStack k = packetByteBuf.readItemStack();
        return new FurnitureRecipe(identifier, string, i, j, defaultedList, k);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, FurnitureRecipe shapedRecipe) {
        packetByteBuf.writeVarInt(shapedRecipe.width);
        packetByteBuf.writeVarInt(shapedRecipe.height);
        packetByteBuf.writeString(shapedRecipe.group);
        for (Ingredient ingredient : shapedRecipe.input) {
            ingredient.write(packetByteBuf);
        }
        packetByteBuf.writeItemStack(shapedRecipe.output);
    }

}
