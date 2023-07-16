package com.unlikepaladin.pfm.compat.forge.cookingforblockheads.menu;

import com.unlikepaladin.pfm.compat.forge.cookingforblockheads.StoveBlockEntityBalm;
import com.unlikepaladin.pfm.compat.forge.cookingforblockheads.menu.slot.StoveResultSlot;
import com.unlikepaladin.pfm.registry.ScreenHandlerIDs;
import net.blay09.mods.cookingforblockheads.menu.IContainerWithDoor;
import net.blay09.mods.cookingforblockheads.menu.slot.OvenResultSlot;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOven;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOvenFuel;
import net.blay09.mods.cookingforblockheads.menu.slot.SlotOvenTool;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class StoveMenuBalm extends ScreenHandler implements IContainerWithDoor {
    private final StoveBlockEntityBalm tileEntity;

    public StoveMenuBalm(int windowId, PlayerInventory playerInventory, StoveBlockEntityBalm oven) {
        super(ScreenHandlerIDs.STOVE_SCREEN_HANDLER, windowId);
        this.tileEntity = oven;
        Inventory container = oven.getContainer();
        int offsetX = oven.hasPowerUpgrade() ? -5 : 0;

        int i;
        for(i = 0; i < 3; ++i) {
            this.addSlot(new Slot(container, i, 84 + i * 18 + offsetX, 19));
        }

        this.addSlot(new SlotOvenFuel(container, 3, 61 + offsetX, 59));

        for(i = 0; i < 3; ++i) {
            this.addSlot(new StoveResultSlot(playerInventory.player, oven, container, i + 4, 142 + offsetX, 41 + i * 18));
        }

        int j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 3; ++j) {
                this.addSlot(new SlotOven(container, 7 + j + i * 3, 84 + j * 18 + offsetX, 41 + i * 18));
            }
        }

        for(i = 0; i < 4; ++i) {
            this.addSlot(new SlotOvenTool(container, 16 + i, 8, 19 + i * 18, i));
        }

        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 30 + j * 18, 111 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 30 + i * 18, 169));
        }

        this.addProperties(oven.getContainerData());
    }

    public StoveBlockEntityBalm getTileEntity() {
        return this.tileEntity;
    }

    public void close(PlayerEntity player) {
        super.close(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (slotIndex >= 7 && slotIndex < 20) {
                if (!this.insertItem(slotStack, 20, 56, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 4 && slotIndex <= 6) {
                if (!this.insertItem(slotStack, 20, 56, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(slotStack, itemStack);
            } else if (slotIndex >= 20) {
                ItemStack smeltingResult = this.tileEntity.getSmeltingResult(slotStack);
                if (OvenBlockEntity.isItemFuel(slotStack)) {
                    if (!this.insertItem(slotStack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!smeltingResult.isEmpty()) {
                    if (!this.insertItem(slotStack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 20 && slotIndex < 47) {
                    if (!this.insertItem(slotStack, 47, 56, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 47 && slotIndex < 56 && !this.insertItem(slotStack, 20, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(slotStack, 20, 47, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
        }

        return itemStack;
    }

    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public boolean isTileEntity(BlockEntity blockEntity) {
        return this.tileEntity == blockEntity;
    }
}