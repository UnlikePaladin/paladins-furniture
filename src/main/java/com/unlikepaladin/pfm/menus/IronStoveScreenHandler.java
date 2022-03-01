package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class IronStoveScreenHandler extends AbstractFurnaceScreenHandler {
    public IronStoveScreenHandler(int i, PlayerInventory playerInventory) {
        super(PaladinFurnitureMod.IRON_STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, i, playerInventory);
    }

    public IronStoveScreenHandler(int i, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(PaladinFurnitureMod.IRON_STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, i, playerInventory, inventory, propertyDelegate);
    }
}