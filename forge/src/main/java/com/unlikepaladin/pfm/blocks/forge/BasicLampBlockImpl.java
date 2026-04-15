package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.LampBlockEntityImpl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class BasicLampBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new LampBlockEntityImpl(pos, state);
    }
}
