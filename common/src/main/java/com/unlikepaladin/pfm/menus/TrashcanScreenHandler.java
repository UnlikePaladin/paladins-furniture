package com.unlikepaladin.pfm.menus;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

public class TrashcanScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    protected final Level level;
    public TrashcanBlockEntity trashcanBlockEntity;
    public TrashcanScreenHandler(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(null, containerId, playerInventory, new SimpleContainer(9));
        trashcanBlockEntity = (TrashcanBlockEntity) level.getBlockEntity(buf.readBlockPos());
    }

    public TrashcanScreenHandler(TrashcanBlockEntity trashcanBlockEntity, int containerId, Inventory playerInventory, Container inventory) {
        super(ScreenHandlerIDs.TRASHCAN_SCREEN_HANDLER, containerId);
        int j;
        int i;
        this.trashcanBlockEntity = trashcanBlockEntity;
        TrashcanScreenHandler.checkContainerSize(inventory, 9);
        this.inventory = inventory;
        this.level = playerInventory.player.getLevel();
        inventory.startOpen(playerInventory.player);
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                this.addSlot(new Slot(inventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @ExpectPlatform
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        throw new AssertionError("Should've been replaced");
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 9 ? !this.moveItemStackTo(itemStack2, 9, 45, true) : !this.moveItemStackTo(itemStack2, 0, 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
        }
        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

}
