package com.unlikepaladin.pfm.fabric.menus.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class GenericOutputSlot extends Slot {
    private final PlayerEntity player;
    private int amount;
    public int maxItemCount;

    public GenericOutputSlot(PlayerEntity player, Inventory inventory, int index, int x, int y, int maxItemCount) {
        super(inventory, index, x, y);
        this.player = player;
        this.maxItemCount = maxItemCount;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }
        return super.takeStack(amount);
    }

    @Override
    public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        super.onTakeItem(player, stack);
        return stack;
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    @Override
    public int getMaxItemCount() {
        return maxItemCount == 0 ? maxItemCount = super.getMaxItemCount() : maxItemCount;
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        stack.onCraft(this.player.world, this.player, this.amount);
        this.amount = 0;
    }
}

