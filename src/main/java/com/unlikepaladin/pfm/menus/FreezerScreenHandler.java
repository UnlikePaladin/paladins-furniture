package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;

public class FreezerScreenHandler
        extends AbstractFreezerScreenHandler {
    public FreezerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, PaladinFurnitureMod.FREEZING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public FreezerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(PaladinFurnitureMod.FREEZER_SCREEN_HANDLER, PaladinFurnitureMod.FREEZING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }


}

