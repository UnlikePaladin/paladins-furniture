package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;

public class StoveScreenHandler extends AbstractFurnaceMenu {
    private final Container inventory;
    public StoveScreenHandler(int containerId, Inventory playerInventory) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, containerId, playerInventory);
        this.inventory = new SimpleContainer(3);
    }

    public StoveScreenHandler(int containerId, Inventory playerInventory, Container inventory, ContainerData dataAccess) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, containerId, playerInventory, inventory, dataAccess);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }
}