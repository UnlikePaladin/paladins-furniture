package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.slot;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class StoveFuelSlot extends Slot {
    private final StoveScreenHandlerBalm menu;

    public StoveFuelSlot(StoveScreenHandlerBalm menu, Inventory container, int i, int x, int y) {
        super(container, i, x, y);
        this.menu = menu;
    }

    public boolean canInsert(ItemStack itemStack) {
     return true;
     //TODO FIX AS SOON AS CFBH is updated
        //   return OvenBlockEntity.isItemFuel(menu.getTileEntity().getWorld(), itemStack);
    }
}