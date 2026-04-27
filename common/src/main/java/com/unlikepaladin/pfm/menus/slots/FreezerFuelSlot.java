package com.unlikepaladin.pfm.menus.slots;

import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.Slot;

public class FreezerFuelSlot
        extends Slot {
    private final AbstractFreezerScreenHandler handler;

    public FreezerFuelSlot(AbstractFreezerScreenHandler handler, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.handler.isFuel(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return FreezerFuelSlot.isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }
    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == (Items.BUCKET);
    }

}
