package com.unlikepaladin.pfm.compat.emi;

import com.unlikepaladin.pfm.recipes.FreezingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;

import java.util.List;
/*

public class EmiFreezingRecipe implements EmiRecipe {
   private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;
    private final AbstractCookingRecipe recipe;
    private final int fuelMultiplier;
    private final boolean infiniBurn;


    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 82;
    }

    @Override
    public int getDisplayHeight() {
        return 38;
    }

    public EmiFreezingRecipe(RecipeHolder<FreezingRecipe> entry) {
        FreezingRecipe recipe = entry.value();
        input = EmiIngredient.of(recipe.getIngredients().get(0));
        output = EmiStack.of(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
        this.recipe = recipe;
        this.id = entry.id();
        this.fuelMultiplier = 2;
        this.infiniBurn = false;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return PaladinFurnitureModEMIPlugin.FREEZER;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addFillingArrow(24, 5, 50 * recipe.cookingTime()).tooltip((mx, my) -> {
            return List.of(ClientTooltipComponent.create(Component.translatable("emi.cooking.time", recipe.cookingTime() / 20f).getVisualOrderText()));
        });
        if (infiniBurn) {
            widgets.addTexture(FreezingWidget.FULL_FREEZER, 1, 24);
        } else {
            widgets.addTexture(FreezingWidget.EMPTY_FREEZER, 1, 24);
            widgets.addAnimatedTexture(FreezingWidget.FULL_FREEZER, 1, 24, 4000 / 2, false, true, true);
        }
        widgets.addSlot(input, 0, 4);
        widgets.addSlot(output, 56, 0).large(true).recipeContext(this);
    }
}*/