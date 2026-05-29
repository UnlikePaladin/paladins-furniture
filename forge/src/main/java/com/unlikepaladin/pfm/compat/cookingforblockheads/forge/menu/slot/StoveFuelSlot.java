package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.slot;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.StoveScreenHandlerBalm;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StoveFuelSlot extends Slot {
    private final StoveScreenHandlerBalm menu;

    public StoveFuelSlot(StoveScreenHandlerBalm menu, Container container, int i, int x, int y) {
        super(container, i, x, y);
        this.menu = menu;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
     return true;
     //TODO FIX AS SOON AS CFBH is updated
        //   return OvenBlockEntity.isItemFuel(menu.getTileEntity().getLevel(), itemStack);
    }
}