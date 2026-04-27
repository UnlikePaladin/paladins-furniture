package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.LongTag;
import net.minecraft.core.BlockPos;

public class ShowerHandleBlockEntity extends BlockEntity {
    protected BlockPos showerOffset;
    public ShowerHandleBlockEntity() {
        super(BlockEntities.SHOWER_HANDLE_BLOCK_ENTITY);
        this.showerOffset = null;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        if (this.showerOffset != null) {
            LongTag showerHeadPos = LongTag.valueOf(this.showerOffset.asLong());
            nbt.put("showerHead", showerHeadPos);
        }
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
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

                level.sendBlockUpdated(showerHeadPos, state, state, 3);
            } else if (this.level.getBlockEntity(showerHeadPos) == null) {
                this.showerOffset = null;
            }
        }
    }
}
