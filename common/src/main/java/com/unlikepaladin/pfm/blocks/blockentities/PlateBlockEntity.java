package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PlateBlockEntity extends BlockEntity implements Clearable {
    protected final DefaultedList<ItemStack> itemInPlate = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public PlateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntities.PLATE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.itemInPlate.clear();
        Inventories.readNbt(nbt, this.itemInPlate);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        this.saveInitialChunkData(nbt);
        return nbt;
    }

    @Override
    public void clear() {
        this.itemInPlate.clear();
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
    }

    public boolean addItem(ItemStack item) {
        if (itemInPlate.get(0).isEmpty()) {
            this.itemInPlate.set(0, item.split(1));
            this.updateListeners();
            return true;
        }
        return false;
    }

    protected NbtCompound saveInitialChunkData(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.itemInPlate, true);
        return nbt;
    }

    public ItemStack getItemInPlate() {
        return itemInPlate.get(0);
    }

    public ItemStack removeItem() {
        ItemStack stack = this.itemInPlate.get(0).copy();
        this.itemInPlate.set(0, ItemStack.EMPTY);
        updateListeners();
        return stack;
    }

    public Inventory getInventory(){
        SimpleInventory inventory = new SimpleInventory(itemInPlate.size());
        for (int i = 0; i < itemInPlate.size(); i++) {
            inventory.setStack(i, itemInPlate.get(i));
        }
        return inventory;
    }
}
