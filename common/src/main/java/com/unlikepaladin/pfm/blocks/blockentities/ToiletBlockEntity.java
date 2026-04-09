package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public class ToiletBlockEntity extends BlockEntity {
    public ToiletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TOILET_BLOCK_ENTITY, pos, state);
    }
    private int flushTimer = 0;

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return super.getUpdateTag(registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putInt("flushTimer", flushTimer);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        flushTimer = nbt.getInt("flushTimer");
        super.loadAdditional(nbt, registryLookup);
    }

    public void setFlushTimer(int flushTimer) {
        this.flushTimer = flushTimer;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ToiletBlockEntity blockEntity) {
        if (state.getValue(BasicToiletBlock.TOILET_STATE) == ToiletState.FLUSHING) {
            if (blockEntity.flushTimer >= 120) {
                BasicToiletBlock.setClean(state, world, pos);
                blockEntity.setFlushTimer(0);
            } else {
                blockEntity.flushTimer++;
            }
        }
    }
}
