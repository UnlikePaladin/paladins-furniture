package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.ShowerHeadBlockEntityImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BasicShowerHeadBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new ShowerHeadBlockEntityImpl(pos, state);
    }
}
