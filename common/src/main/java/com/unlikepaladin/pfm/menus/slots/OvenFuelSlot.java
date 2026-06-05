package com.unlikepaladin.pfm.menus.slots;

import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class OvenFuelSlot extends Slot {
    private final OvenScreenHandler menu;

    public OvenFuelSlot(OvenScreenHandler abstractFurnaceMenu, Container container, int i, int j, int k) {
        super(container, i, j, k);
        this.menu = abstractFurnaceMenu;
    }

    public boolean mayPlace(ItemStack itemStack) {
        return this.menu.isFuel(itemStack) || isBucket(itemStack);
    }

    public int getMaxStackSize(ItemStack itemStack) {
        return isBucket(itemStack) ? 1 : super.getMaxStackSize(itemStack);
    }

    public static boolean isBucket(ItemStack itemStack) {
        return itemStack.getItem() == (Items.BUCKET);
    }
}
