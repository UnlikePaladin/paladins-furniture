package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.LongTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY, pos, state);
        this.showerOffset = null;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (this.showerOffset != null) {
            LongTag showerHeadPos = LongTag.valueOf(this.showerOffset.asLong());
            nbt.put("showerHead", showerHeadPos);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        if(nbt.contains("showerHead", Tag.TAG_LONG)){
            this.showerOffset = BlockPos.of(nbt.getLong("showerHead"));
        }
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
