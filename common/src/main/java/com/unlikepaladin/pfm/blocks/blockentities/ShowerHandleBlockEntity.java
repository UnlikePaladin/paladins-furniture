package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY, pos, state);
        this.showerOffset = null;
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (this.showerOffset != null) {
            view.putLong("showerHead", this.showerOffset.asLong());
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        view.getOptionalLong("showerHead").ifPresent(
                aLong -> this.showerOffset = BlockPos.fromLong(aLong));
    }

    public void setShowerOffset(BlockPos showerOffset) {
        this.showerOffset = showerOffset;
    }

    public void setState(boolean open)
    {
        if (this.showerOffset != null) {
            BlockPos showerHeadPos = this.pos.subtract(this.showerOffset);
            if(this.world.getBlockEntity(showerHeadPos) != null) {

                BlockState state = world.getBlockState(showerHeadPos);
                ((ShowerHeadBlockEntity)world.getBlockEntity(showerHeadPos)).setOpen(open);

                world.updateListeners(showerHeadPos, state, state, Block.NOTIFY_LISTENERS);
            } else if (this.world.getBlockEntity(showerHeadPos) == null) {
                this.showerOffset = null;
            }
        }
    }
}
