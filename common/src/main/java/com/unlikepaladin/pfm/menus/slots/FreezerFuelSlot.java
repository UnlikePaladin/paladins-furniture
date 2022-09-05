package com.unlikepaladin.pfm.menus.slots;

import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class FreezerFuelSlot
        extends Slot {
    private final AbstractFreezerScreenHandler handler;

    public FreezerFuelSlot(AbstractFreezerScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.handler.isFuel(stack);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return FreezerFuelSlot.isBucket(stack) ? 1 : super.getMaxItemCount(stack);
    }
    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == (Items.BUCKET);
    }

}
