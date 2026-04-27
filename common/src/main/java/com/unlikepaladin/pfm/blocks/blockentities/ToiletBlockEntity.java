package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public class ToiletBlockEntity extends BlockEntity implements Tickable {
    public ToiletBlockEntity() {
        super(BlockEntities.TOILET_BLOCK_ENTITY);
    }
    private int flushTimer = 0;

    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("flushTimer", flushTimer);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        flushTimer = nbt.getInt("flushTimer");
        super.load(state, nbt);
    }

    public void setFlushTimer(int flushTimer) {
        this.flushTimer = flushTimer;
    }

    @Override
    public void tick() {
        BlockState state = this.getBlockState();
        if (state.getValue(BasicToiletBlock.TOILET_STATE) == ToiletState.FLUSHING) {
            if (this.flushTimer >= 120) {
                BasicToiletBlock.setClean(state, world, pos);
                this.setFlushTimer(0);
            } else {
                this.flushTimer++;
            }
        }
    }
}
