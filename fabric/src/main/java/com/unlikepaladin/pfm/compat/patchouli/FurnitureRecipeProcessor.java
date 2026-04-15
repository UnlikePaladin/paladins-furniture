package com.unlikepaladin.pfm.compat.patchouli;

import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FurnitureRecipeProcessor implements IComponentProcessor {
    @Override
    public void setup(Level level, IVariableProvider variables) {

    }

    @Override
    public IVariable process(Level level, String key) {
        return null;
    }
 /*   private FurnitureRecipe recipe;
    private ResourceLocation variant;
    private boolean isBase;
    @Override
    public void setup(Level level, IVariableProvider variables) {
        String recipeId = variables.get("recipe", level.registryAccess()).asString();
        RecipeManager manager = level.recipeAccess();
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
                    if (index >= innerRecipe.getIngredientPlacement().getIngredients().size()) {
                        ingredientsArr[i] = ItemStack.EMPTY;
                        continue;
                    }
                    Ingredient ingredient = innerRecipe.getIngredientPlacement().getIngredients().get(index);
                    List<Holder<Item>> stacks = ingredient.getMatchingItems();
                    ingredientsArr[i] = stacks.isEmpty() ? ItemStack.EMPTY : stacks.get(0).value().getDefaultStack();
                }
                return IVariable.from(ingredientsArr, level.registryAccess());
            } else if (key.equals("resultitem")) {
                ItemStack[] resultsArr = new ItemStack[innerRecipeList.size()];
                for (int i = 0; i < innerRecipeList.size(); i++) {
                    FurnitureRecipe.CraftableFurnitureRecipe innerRecipe = innerRecipeList.get(i);
                    resultsArr[i] = innerRecipe.getResult(level.getRegistryManager());
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