package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class StoveScreenHandler extends AbstractFurnaceScreenHandler {
    public StoveScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory);
    }

    public StoveScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
    }

    public StoveScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookCategory.SMOKER, syncId, playerInventory);
    }
}