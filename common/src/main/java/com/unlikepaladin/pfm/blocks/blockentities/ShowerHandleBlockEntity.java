package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.LongTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;

import java.util.Optional;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY, pos, state);
        this.showerOffset = null;
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        if (this.showerOffset != null) {
            view.putLong("showerHead", this.showerOffset.asLong());
        }
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        view.getLong("showerHead").ifPresent(
                aLong -> this.showerOffset = BlockPos.of(aLong));
    }

    public void setState(boolean open)
    {
        if (this.showerOffset != null) {
            BlockPos showerHeadPos = this.worldPosition.subtract(this.showerOffset);
            if(this.level.getBlockEntity(showerHeadPos) != null) {

                BlockState state = level.getBlockState(showerHeadPos);
                ((ShowerHeadBlockEntity)level.getBlockEntity(showerHeadPos)).setOpen(open);

                level.sendBlockUpdated(showerHeadPos, state, state, Block.UPDATE_CLIENTS);
            } else if (this.level.getBlockEntity(showerHeadPos) == null) {
                this.showerOffset = null;
            }
        }
    }
}
