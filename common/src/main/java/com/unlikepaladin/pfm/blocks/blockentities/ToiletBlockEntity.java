package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ToiletBlockEntity extends BlockEntity {
    public ToiletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.TOILET_BLOCK_ENTITY, pos, state);
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
    public void readNbt(NbtCompound nbt) {
        flushTimer = nbt.getInt("flushTimer");
        super.readNbt(nbt);
    }

    public void setFlushTimer(int flushTimer) {
        this.flushTimer = flushTimer;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ToiletBlockEntity blockEntity) {
        if (state.get(BasicToiletBlock.TOILET_STATE) == ToiletState.FLUSHING) {
            if (blockEntity.flushTimer >= 120) {
                BasicToiletBlock.setClean(state, world, pos);
                blockEntity.setFlushTimer(0);
            } else {
                blockEntity.flushTimer++;
            }
        }
    }
}
