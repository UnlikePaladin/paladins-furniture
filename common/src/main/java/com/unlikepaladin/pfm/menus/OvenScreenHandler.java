package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;

public class OvenScreenHandler extends AbstractFurnaceScreenHandler {
    private final Inventory inventory;
    public OvenScreenHandler(int syncId, PlayerInventory playerInventory, StoveScreenHandler.StoveData data) {
        super(ScreenHandlerIDs.OVEN_SCREEN_HANDLER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, syncId, playerInventory, (Inventory) playerInventory.player.getEntityWorld().getBlockEntity(data.pos()), new ArrayPropertyDelegate(4));
        this.inventory = (Inventory) playerInventory.player.getEntityWorld().getBlockEntity(data.pos());
        inventory.onOpen(playerInventory.player);
    }

    public OvenScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlerIDs.OVEN_SCREEN_HANDLER, RecipeType.SMOKING, RecipePropertySet.SMOKER_INPUT, RecipeBookType.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}