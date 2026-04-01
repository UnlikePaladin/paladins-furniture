package com.unlikepaladin.pfm.menus.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class GenericOutputSlot extends Slot {
    private final Player player;
    private int amount;
    public int maxItemCount;

    public GenericOutputSlot(Player player, Container inventory, int index, int x, int y, int maxItemCount) {
        super(inventory, index, x, y);
        this.player = player;
        this.maxItemCount = maxItemCount;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    public int getMaxStackSize() {
        return maxItemCount == 0 ? maxItemCount = super.getMaxStackSize() : maxItemCount;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.getLevel(), this.player, this.amount);
        this.amount = 0;
    }
}

