package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;

public class MicrowaveScreenHandler
        extends AbstractMicrowaveScreenHandler {
    public MicrowaveScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(PaladinFurnitureMod.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory);
    }

    public MicrowaveScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(PaladinFurnitureMod.MICROWAVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
    }


}

