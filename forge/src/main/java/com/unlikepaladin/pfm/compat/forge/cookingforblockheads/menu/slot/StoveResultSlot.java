package com.unlikepaladin.pfm.compat.forge.cookingforblockheads.menu.slot;

import com.unlikepaladin.pfm.compat.forge.cookingforblockheads.StoveBlockEntityBalm;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class StoveResultSlot extends Slot {
    private final PlayerEntity player;
    private final StoveBlockEntityBalm tileEntity;
    private int removeCount;

    public StoveResultSlot(PlayerEntity player, StoveBlockEntityBalm tileEntity, Inventory container, int i, int x, int y) {
        super(container, i, x, y);
        this.player = player;
        this.tileEntity = tileEntity;
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public void onQuickTransfer(ItemStack oldStack, ItemStack newStack) {
        int amount = newStack.getCount() - oldStack.getCount();
        if (amount > 0) {
            this.onCrafted(newStack, amount);
        }

    }

    public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
        this.onCrafted(itemStack);
        super.onTakeItem(player, itemStack);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafted(stack);
    }

    protected void onCrafted(ItemStack stack) {
        stack.onCraft(this.player.world, this.player, this.removeCount);
        this.removeCount = 0;
        if (this.tileEntity.getWorld() != null && !stack.isEmpty()) {
            Balm.getEvents().fireEvent(new OvenItemSmeltedEvent(this.player, this.tileEntity.getWorld(), this.tileEntity.getPos(), stack));
        }

    }
}