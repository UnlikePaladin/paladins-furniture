package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Clearable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class PlateBlockEntity extends BlockEntity implements Clearable {
    private final DefaultedList<ItemStack> itemInPlate = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public PlateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.PLATE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.itemInPlate.clear();
        Inventories.readNbt(nbt, this.itemInPlate);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        this.saveInitialChunkData(nbt);
    }

    @Override
    public void clear() {
        this.itemInPlate.clear();
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public boolean addItem(ItemStack item) {
        if (!itemInPlate.isEmpty()) {
            this.itemInPlate.set(0, item.split(1));
            this.updateListeners();
            return true;
        }
        return false;
    }

    private NbtCompound saveInitialChunkData(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.itemInPlate, true);
        return nbt;
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.saveInitialChunkData(new NbtCompound());
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
