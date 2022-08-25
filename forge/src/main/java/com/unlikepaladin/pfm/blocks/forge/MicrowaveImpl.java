package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MicrowaveImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntityImpl(pos, state);
    }
}
