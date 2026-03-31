package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FreezingRecipeProcessor implements IComponentProcessor {
    private Recipe<?> recipe;
    @Override
    public void setup(Level level, IVariableProvider variables) {
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
                    ItemStack[] stacks = ingredient.getItems();
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
