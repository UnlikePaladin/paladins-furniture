package com.unlikepaladin.pfm.menus.slots;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class OvenResultSlot<T extends OvenBlockEntity> extends Slot {
    private final Player player;
    private int removeCount;

    public OvenResultSlot(Player player, Container container, int i, int x, int y) {
        super(container, i, x, y);
        this.player = player;
    }

    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
        int amount = newStack.getCount() - oldStack.getCount();
        if (amount > 0) {
            this.onQuickCraft(newStack, amount);
        }

    }

    public void onTake(Player player, ItemStack itemStack) {
        this.checkTakeAchievements(itemStack);
        super.onTake(player, itemStack);
    }

    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        this.removeCount = 0;
        if (this.container instanceof OvenBlockEntity ovenBlockEntity && ovenBlockEntity.getLevel() != null && !stack.isEmpty()) {
            if (this.player instanceof ServerPlayer) {
                ovenBlockEntity.awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
            }
        }

    }
}
