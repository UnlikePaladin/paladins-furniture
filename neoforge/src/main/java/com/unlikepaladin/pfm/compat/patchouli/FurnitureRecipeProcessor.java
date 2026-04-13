package com.unlikepaladin.pfm.compat.patchouli;

import com.unlikepaladin.pfm.recipes.FurnitureRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;

public class FurnitureRecipeProcessor implements IComponentProcessor {
    @Override
    public void setup(World level, IVariableProvider variables) {

    }

    @Override
    public IVariable process(World level, String key) {
        return null;
    }
    /*
    private FurnitureRecipe recipe;
    private ResourceLocation variant;
private boolean isBase;
    @Override
    public void setup(Level level, IVariableProvider variables) {
        String recipeId = variables.get("recipe", level.registryAccess()).asString();
        RecipeManager manager = level.getRecipeManager();
        Recipe<?> recipe = manager.byKey(ResourceLocation.parse(recipeId)).map(RecipeHolder::value).orElse(null);
        this.recipe = recipe instanceof FurnitureRecipe ? (FurnitureRecipe) recipe : null;
        this.variant = variables.has("variant") ? ResourceLocation.tryParse(variables.get("variant", level.registryAccess()).asString()) : null;
    }

    @Override
    public @NotNull IVariable process(Level level, String key) {
        if (recipe != null) {
            List<? extends FurnitureRecipe.CraftableFurnitureRecipe> innerRecipeList;
            if (variant != null) {
                innerRecipeList = recipe.getInnerRecipesForVariant(variant);
            } else {
                innerRecipeList = recipe.getInnerRecipes();
            }
            if (key.startsWith("item")) {
                int index = Integer.parseInt(key.substring(4)) - 1;
                ItemStack[] ingredientsArr = new ItemStack[innerRecipeList.size()];
                for (int i = 0; i < innerRecipeList.size(); i++) {
                    FurnitureRecipe.CraftableFurnitureRecipe innerRecipe = innerRecipeList.get(i);
                    if (index >= innerRecipe.getIngredients().size()) {
                        ingredientsArr[i] = ItemStack.EMPTY;
                        continue;
                    }
                    Ingredient ingredient = innerRecipe.getIngredients().get(index);
                    ItemStack[] stacks = ingredient.getItems();
                    ingredientsArr[i] = stacks.length == 0 ? ItemStack.EMPTY : stacks[0];
                }
                return IVariable.from(ingredientsArr, level.registryAccess());
            } else if (key.equals("resultitem")) {
                ItemStack[] resultsArr = new ItemStack[innerRecipeList.size()];
                for (int i = 0; i < innerRecipeList.size(); i++) {
                    FurnitureRecipe.CraftableFurnitureRecipe innerRecipe = innerRecipeList.get(i);
                    resultsArr[i] = innerRecipe.getResultItem(level.registryAccess());
                }
                return IVariable.from(resultsArr, level.registryAccess());
            } else if (key.equals("icon")) {
                ItemStack icon = recipe.getToastSymbol();
                return IVariable.from(icon, level.registryAccess());
            } else if (key.equals("text")) {
                return IVariable.wrap(recipe.getOutputCount(level.registryAccess()) + "x$(br)" + recipe.getName(level.registryAccess()));
            } else if (key.equals("icount")) {
                return IVariable.wrap(recipe.getOutputCount(level.registryAccess()));
            } else if (key.equals("iname")) {
                return IVariable.wrap(recipe.getName(level.registryAccess()));
            }
        }
        return IVariable.empty();
    }

    @Override
    public void refresh(Screen parent, int left, int top) {
        IComponentProcessor.super.refresh(parent, left, top);
    }*/
}