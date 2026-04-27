package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FreezingRecipeProcessor implements IComponentProcessor {
    private Recipe<?> recipe;
    @Override
    public void setup(IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        recipe = manager.byKey(new ResourceLocation(recipeId)).orElse(null);
    }

    @Override
    public IVariable process(String key) {
     if (recipe != null) {
         switch (key) {
             case "ingredient":
                 Ingredient ingredient = recipe.getIngredients().get(0);
                 ItemStack[] stacks = ingredient.getItems();
                 ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];

                 return IVariable.from(stack);
             case "output":
                 ItemStack result = recipe.getResultItem();
                 return IVariable.from(result);
             case "icon":
                 ItemStack icon = recipe.getToastSymbol();
                 return IVariable.from(icon);
             case "text":
                 ItemStack out = recipe.getResultItem();
                 return IVariable.wrap(out.getCount() + "x$(br)" + out.getHoverName());
             case "icount":
                 return IVariable.wrap(recipe.getResultItem().getCount());
             case "iname":
                 return IVariable.wrap(recipe.getResultItem().getHoverName().getString());
         }
        }
     return null;
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
