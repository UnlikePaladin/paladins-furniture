package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;

public class IronStoveScreenHandler extends AbstractFurnaceMenu {
    private final Container inventory;
    public IronStoveScreenHandler(int i, Inventory playerInventory) {
        super(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, i, playerInventory);
        this.inventory = new SimpleContainer(3);
    }

    public IronStoveScreenHandler(int i, Inventory playerInventory, Container inventory, ContainerData dataAccess) {
        super(ScreenHandlerIDs.IRON_STOVE_SCREEN_HANDLER, RecipeType.SMOKING, RecipeBookType.SMOKER, i, playerInventory, inventory, dataAccess);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }
}