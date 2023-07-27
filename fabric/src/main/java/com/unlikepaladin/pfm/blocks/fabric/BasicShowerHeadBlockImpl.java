package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.ShowerHeadBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BasicShowerHeadBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new ShowerHeadBlockEntityImpl(pos, state);
    }
}
