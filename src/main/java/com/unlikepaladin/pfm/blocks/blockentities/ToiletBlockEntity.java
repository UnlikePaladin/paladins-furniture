package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ToiletBlockEntity extends BlockEntity {
    public ToiletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TOILET_BLOCK_ENTITY, pos, state);
    }
    private int flushTimer = 0;
    public static void tick(World world, BlockPos pos, BlockState state, ToiletBlockEntity blockEntity) {
        if (state.get(BasicToilet.TOILET_STATE) == ToiletState.FLUSHING) {
            if (blockEntity.flushTimer >= 50) {
                BasicToilet.setClean(state, world, pos);
                blockEntity.flushTimer = 0;
            } else {
                blockEntity.flushTimer++;
            }
        }
    }
}
