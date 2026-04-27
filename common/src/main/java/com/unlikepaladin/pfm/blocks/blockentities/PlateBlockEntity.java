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

import java.util.function.Supplier;

public class PlateBlockEntity extends BlockEntity implements Clearable {
    protected final NonNullList<ItemStack> itemInPlate = NonNullList.withSize(1, ItemStack.EMPTY);
    public PlateBlockEntity() {
        super(BlockEntities.PLATE_BLOCK_ENTITY);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.itemInPlate.clear();
        ContainerHelper.loadAllItems(nbt, this.itemInPlate);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        this.saveInitialChunkData(nbt);
        return nbt;
    }

    @Override
    public void clearContent() {
        this.itemInPlate.clear();
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    private void sendBlockUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
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
        super.save(nbt);
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
    public static Supplier<? extends PlateBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
