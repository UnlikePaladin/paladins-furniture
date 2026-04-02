package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PlateBlockEntity extends BlockEntity implements Clearable {
    protected final NonNullList<ItemStack> itemInPlate = NonNullList.withSize(1, ItemStack.EMPTY);
    public PlateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntities.PLATE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.itemInPlate.clear();
        ContainerHelper.loadAllItems(nbt, this.itemInPlate);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        this.saveInitialChunkData(nbt);
    }

    @Override
    public void clearContent() {
        this.itemInPlate.clear();
        level.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    private void sendBlockUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public boolean addItem(ItemStack item) {
        if (itemInPlate.get(0).isEmpty()) {
            this.itemInPlate.set(0, item.split(1));
            this.sendBlockUpdated();
            return true;
        }
        return false;
    }

    protected CompoundTag saveInitialChunkData(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.itemInPlate, true);
        return nbt;
    }

    public ItemStack getItemInPlate() {
        return itemInPlate.get(0);
    }

    public ItemStack removeItem() {
        ItemStack stack = this.itemInPlate.get(0).copy();
        this.itemInPlate.set(0, ItemStack.EMPTY);
        sendBlockUpdated();
        return stack;
    }

    public Container getContainer(){
        SimpleContainer inventory = new SimpleContainer(itemInPlate.size());
        for (int i = 0; i < itemInPlate.size(); i++) {
            inventory.setItem(i, itemInPlate.get(i));
        }
        return inventory;
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends PlateBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
