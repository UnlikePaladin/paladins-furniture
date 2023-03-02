package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.StoveBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class StoveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntityImpl(BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
    }
}
