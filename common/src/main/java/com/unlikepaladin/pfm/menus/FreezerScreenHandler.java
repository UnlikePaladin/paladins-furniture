package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;

public class FreezerScreenHandler extends AbstractFreezerScreenHandler {
    public FreezerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, RecipeTypes.FREEZING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public FreezerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, RecipeTypes.FREEZING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }


    public FreezerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        super(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, RecipeTypes.FREEZING_RECIPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }
}

