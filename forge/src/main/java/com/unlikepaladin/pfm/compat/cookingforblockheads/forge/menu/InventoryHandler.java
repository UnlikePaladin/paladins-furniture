package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;

import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.CounterOvenBlockEntityBalm;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class InventoryHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NbtCompound> {

    private List<ItemStack> inventory;

    public InventoryHandler(List<ItemStack> inventory) {
        this.inventory = inventory;
    }
    @Override
    public int getSlots() {
        return this.inventory.size();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.inventory.set(slot, stack);
    }

    public void setSize(int size) {
        this.inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    protected void onContentsChanged(int slot) {
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isItemValid(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.inventory.get(slot);
            int limit = this.getStackLimit(slot, stack);
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        this.inventory.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                    } else {
                        existing.increment(reachedLimit ? limit : stack.getCount());
                    }

                    this.onContentsChanged(slot);
                }

                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.inventory.get(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxCount());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        this.inventory.set(slot, ItemStack.EMPTY);
                        this.onContentsChanged(slot);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        this.inventory.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                        this.onContentsChanged(slot);
                    }

                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return true;
    }

    public NbtCompound serializeNBT() {
        NbtList nbtTagList = new NbtList();

        for(int i = 0; i < this.inventory.size(); ++i) {
            if (!this.inventory.get(i).isEmpty()) {
                NbtCompound itemTag = new NbtCompound();
                itemTag.putInt("Slot", i);
                this.inventory.get(i).writeNbt(itemTag);
                nbtTagList.add(itemTag);
            }
        }

        NbtCompound nbt = new NbtCompound();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", this.inventory.size());
        return nbt;
    }

    public void deserializeNBT(NbtCompound nbt) {
        this.setSize(nbt.contains("Size", 3) ? nbt.getInt("Size") : this.inventory.size());
        NbtList tagList = nbt.getList("Items", 10);

        for(int i = 0; i < tagList.size(); ++i) {
            NbtCompound itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < this.inventory.size()) {
                this.inventory.set(slot, ItemStack.fromNbt(itemTags));
            }
        }

        this.onLoad();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.inventory.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.inventory.size() + ")");
        }
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(this.getSlotLimit(slot), stack.getMaxCount());
    }

    protected void onLoad() {
    }
}
