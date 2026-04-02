package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
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
        recipe = manager.byKey(new ResourceLocation(recipeId)).map(RecipeHolder::value).orElse(null);
    }

    @Override
    public @NotNull IVariable process(Level level, String key) {
        if (recipe != null) {
            switch (key) {
                case "ingredient":
                    Ingredient ingredient = recipe.getIngredients().get(0);
                    ItemStack[] stacks = ingredient.getItems();
                    ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];

                    return IVariable.from(stack);
                case "output":
                    ItemStack result = recipe.getResultItem(level.registryAccess());
                    return IVariable.from(result);
                case "icon":
                    ItemStack icon = recipe.getToastSymbol();
                    return IVariable.from(icon);
                case "text":
                    ItemStack out = recipe.getResultItem(level.registryAccess());
                    return IVariable.wrap(out.getCount() + "x$(br)" + out.getHoverName());
                case "icount":
                    return IVariable.wrap(recipe.getResultItem(level.registryAccess()).getCount());
                case "iname":
                    return IVariable.wrap(recipe.getResultItem(level.registryAccess()).getHoverName().getString());
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
