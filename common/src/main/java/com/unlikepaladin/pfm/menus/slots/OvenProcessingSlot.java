package com.unlikepaladin.pfm.menus.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OvenProcessingSlot extends Slot {

    public OvenProcessingSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return 1;
    }
}
