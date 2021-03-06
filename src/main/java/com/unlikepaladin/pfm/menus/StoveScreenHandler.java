package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlersRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class StoveScreenHandler extends AbstractFurnaceScreenHandler {
    public StoveScreenHandler(int i, PlayerInventory playerInventory) {
        super(ScreenHandlersRegistry.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, i, playerInventory);
    }

    public StoveScreenHandler(int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlersRegistry.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, i, playerInventory, inventory, propertyDelegate);
    }
}