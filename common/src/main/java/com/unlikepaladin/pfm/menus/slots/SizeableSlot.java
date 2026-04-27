package com.unlikepaladin.pfm.menus.slots;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SizeableSlot extends Slot {
    private int amount;
    private final Player player;
    public SizeableSlot(Player player, Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.amount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public ItemStack onTake(Player player, ItemStack stack) {
        this.checkTakeAchievements(stack);
        stack = super.onTake(player, stack);
        return stack;
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.amount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.amount);
        this.amount = 0;
    }
}
