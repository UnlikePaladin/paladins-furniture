package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ToiletBlockEntity extends BlockEntity implements Tickable {
    public ToiletBlockEntity() {
        super(BlockEntities.TOILET_BLOCK_ENTITY);
    }
    private int flushTimer = 0;

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return super.toInitialChunkDataNbt();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("flushTimer", flushTimer);
        return nbt;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        flushTimer = nbt.getInt("flushTimer");
        super.fromTag(state, nbt);
    }

    public void setFlushTimer(int flushTimer) {
        this.flushTimer = flushTimer;
    }

    @Override
    public void tick() {
        BlockState state = this.getCachedState();
        if (state.get(BasicToiletBlock.TOILET_STATE) == ToiletState.FLUSHING) {
            if (this.flushTimer >= 120) {
                BasicToiletBlock.setClean(state, world, pos);
                this.setFlushTimer(0);
            } else {
                this.flushTimer++;
            }
        }
    }
}
