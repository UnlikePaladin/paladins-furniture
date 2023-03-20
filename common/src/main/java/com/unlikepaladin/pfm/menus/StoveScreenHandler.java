package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class StoveScreenHandler extends AbstractFurnaceScreenHandler {
    private final Inventory inventory;
    public StoveScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory);
        this.inventory = new SimpleInventory(3);
    }

    public StoveScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }
}