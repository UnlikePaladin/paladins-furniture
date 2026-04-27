package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.slot;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;

public class StoveResultSlot extends SlotItemHandler {
    private final Player player;
    private final StoveBlockEntityBalm tileEntity;
    private int removeCount;

    public StoveResultSlot(Player player, StoveBlockEntityBalm tileEntity, IItemHandler container, int i, int x, int y) {
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

    public ItemStack onTakeItem(Player player, ItemStack itemStack) {
        this.onCrafted(itemStack);
        return super.onTake(player, itemStack);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafted(stack);
    }

    protected void onCrafted(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        this.removeCount = 0;
        if (this.tileEntity.getLevel() != null && !stack.isEmpty()) {
            MinecraftForge.EVENT_BUS.post(new OvenItemSmeltedEvent(this.player, this.tileEntity.getLevel(), this.tileEntity.getBlockPos(), stack));
        }

    }
}