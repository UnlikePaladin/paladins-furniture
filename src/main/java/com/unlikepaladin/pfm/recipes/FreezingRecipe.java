package com.unlikepaladin.pfm.recipes;

import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.registry.RecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class FreezingRecipe extends AbstractCookingRecipe {

    public FreezingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(RecipeRegistry.FREEZING_RECIPE, id, group, input, output, experience, cookTime);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlockItemRegistry.WHITE_FRIDGE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.FREEZING_RECIPE_SERIALIZER;
    }


}
