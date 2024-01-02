package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.LampBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BasicLampBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new LampBlockEntityImpl(pos, state);
    }
}
