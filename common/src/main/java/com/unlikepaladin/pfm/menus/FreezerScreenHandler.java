package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import com.unlikepaladin.pfm.registry.RecipeTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ContainerData;

public class FreezerScreenHandler extends AbstractFreezerScreenHandler {
    public FreezerScreenHandler(int containerId, Inventory playerInventory) {
        super(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, RecipeTypes.FREEZING_RECIPE, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public FreezerScreenHandler(int containerId, Inventory playerInventory, Container inventory, ContainerData dataAccess) {
        super(ScreenHandlerIDs.FREEZER_SCREEN_HANDLER, RecipeTypes.FREEZING_RECIPE, RecipeBookType.FURNACE, containerId, playerInventory, inventory, dataAccess);
    }
}

