package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FurnitureRecipeProcessor implements IComponentProcessor {
    private Recipe<?> recipe;
    @Override
    public void setup(IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
        recipe = manager.get(new Identifier(recipeId)).orElse(null);
    }

    @Override
    public IVariable process(String key) {
        if (recipe != null) {
            if (key.startsWith("item")) {
                int index = Integer.parseInt(key.substring(4)) - 1;
                if (index >= recipe.getIngredients().size()) {
                    return IVariable.from(ItemStack.EMPTY);
                }
                Ingredient ingredient = recipe.getIngredients().get(index);
                ItemStack[] stacks = ingredient.getMatchingStacks();
                ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
                return IVariable.from(stack);
            } else if (key.equals("resultitem")) {
                ItemStack result = recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager());
                return IVariable.from(result);
            } else if (key.equals("icon")) {
                ItemStack icon = recipe.createIcon();
                return IVariable.from(icon);
            } else if (key.equals("text")) {
                ItemStack out = recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager());
                return IVariable.wrap(out.getCount() + "x$(br)" + out.getName());
            } else if (key.equals("icount")) {
                return IVariable.wrap(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()).getCount());
            } else if (key.equals("iname")) {
                return IVariable.wrap(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()).getName().getString());
            }
        }
        return null;
    }

    @Override
    public void refresh(Screen parent, int left, int top) {
        IComponentProcessor.super.refresh(parent, left, top);
    }
}