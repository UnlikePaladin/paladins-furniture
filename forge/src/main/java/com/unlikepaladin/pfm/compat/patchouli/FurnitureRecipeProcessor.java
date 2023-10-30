package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FurnitureRecipeProcessor implements IComponentProcessor {
    private Recipe<?> recipe;
    @Override
    public void setup(World level, IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = level.getRecipeManager();
        recipe = manager.get(new Identifier(recipeId)).map(RecipeEntry::value).orElse(null);
    }

    @Override
    public @NotNull IVariable process(World level, String key) {
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
                ItemStack result = recipe.getResult(level.getRegistryManager());
                return IVariable.from(result);
            } else if (key.equals("icon")) {
                ItemStack icon = recipe.createIcon();
                return IVariable.from(icon);
            } else if (key.equals("text")) {
                ItemStack out = recipe.getResult(level.getRegistryManager());
                return IVariable.wrap(out.getCount() + "x$(br)" + out.getName());
            } else if (key.equals("icount")) {
                return IVariable.wrap(recipe.getResult(level.getRegistryManager()).getCount());
            } else if (key.equals("iname")) {
                return IVariable.wrap(recipe.getResult(level.getRegistryManager()).getName().getString());
            }
        }
        return IVariable.empty();
    }

    @Override
    public void refresh(Screen parent, int left, int top) {
        IComponentProcessor.super.refresh(parent, left, top);
    }
}