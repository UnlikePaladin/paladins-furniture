package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FreezingRecipeProcessor implements IComponentProcessor {
    private Recipe<?> recipe;
    @Override
    public void setup(World level, IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = level.getRecipeManager();
        recipe = manager.get(new Identifier(recipeId)).orElse(null);
    }

    @Override
    public @NotNull IVariable process(World level, String key) {
     if (recipe != null) {
         switch (key) {
             case "ingredient":
                 Ingredient ingredient = recipe.getIngredients().get(0);
                 ItemStack[] stacks = ingredient.getMatchingStacks();
                 ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];

                 return IVariable.from(stack);
             case "output":
                 ItemStack result = recipe.getOutput(level.getRegistryManager());
                 return IVariable.from(result);
             case "icon":
                 ItemStack icon = recipe.createIcon();
                 return IVariable.from(icon);
             case "text":
                 ItemStack out = recipe.getOutput(level.getRegistryManager());
                 return IVariable.wrap(out.getCount() + "x$(br)" + out.getName());
             case "icount":
                 return IVariable.wrap(recipe.getOutput(level.getRegistryManager()).getCount());
             case "iname":
                 return IVariable.wrap(recipe.getOutput(level.getRegistryManager()).getName().getString());
         }
        }
     return IVariable.empty();
    }

    @Override
    public void refresh(Screen parent, int left, int top) {
        IComponentProcessor.super.refresh(parent, left, top);
    }

    @Override
    public boolean allowRender(String group) {
        return recipe != null;
    }
}
