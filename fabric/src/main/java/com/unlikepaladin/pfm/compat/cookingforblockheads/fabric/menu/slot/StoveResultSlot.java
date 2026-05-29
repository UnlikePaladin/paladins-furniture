package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu.slot;

import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.StoveBlockEntityBalm;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class StoveResultSlot extends Slot {
    private final Player player;
    private final StoveBlockEntityBalm tileEntity;
    private int removeCount;

    public StoveResultSlot(Player player, StoveBlockEntityBalm tileEntity, Container container, int i, int x, int y) {
        super(container, i, x, y);
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
        int amount = newStack.getCount() - oldStack.getCount();
        if (amount > 0) {
            this.onCrafted(newStack, amount);
        }

    }

    public void onTake(Player player, ItemStack itemStack) {
        this.onCrafted(itemStack);
        super.onTake(player, itemStack);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafted(stack);
    }

    protected void onCrafted(ItemStack stack) {
        stack.onCraftedBy(this.player, this.removeCount);
        this.removeCount = 0;
        if (this.tileEntity.getLevel() != null && !stack.isEmpty()) {
            // TODO: FIX AS SOON AS CFBH updates
            //Balm.getEvents().fireEvent(new OvenItemSmeltedEvent(this.player, this.tileEntity.getLevel(), this.tileEntity.getBlockPos(), stack));
        }

    }
}