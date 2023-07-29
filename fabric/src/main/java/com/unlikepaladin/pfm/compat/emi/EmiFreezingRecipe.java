package com.unlikepaladin.pfm.compat.emi;

import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiCookingRecipe;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.recipe.AbstractCookingRecipe;

import java.util.List;


public class EmiFreezingRecipe extends EmiCookingRecipe {
    private final AbstractCookingRecipe recipe;
    private final boolean infiniBurn = false;
    private final EmiIngredient input;
    private final EmiStack output;
    public EmiFreezingRecipe(FreezingRecipe recipe) {
        super(recipe, PaladinFurnitureModEMIPlugin.FREEZER, 2, false);
        input = EmiIngredient.of(recipe.getIngredients().get(0));
        output = EmiStack.of(recipe.getOutput());
        this.recipe = recipe;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PaladinFurnitureModEMIPlugin.FREEZER;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(24, 5, 50 * recipe.getCookTime()).tooltip((mx, my) -> {
            return List.of(TooltipComponent.of(EmiPort.translatable("emi.cooking.time", recipe.getCookTime() / 20f).asOrderedText()));
        });
        if (infiniBurn) {
            widgets.addTexture(FreezingWidget.FULL_FREEZER, 1, 24);
        } else {
            widgets.addTexture(FreezingWidget.EMPTY_FREEZER, 1, 24);
            widgets.addAnimatedTexture(FreezingWidget.FULL_FREEZER, 1, 24, 4000 / 2, false, true, true);
        }
        widgets.addSlot(input, 0, 4);
        widgets.addSlot(output, 56, 0).output(true).recipeContext(this);
    }
}