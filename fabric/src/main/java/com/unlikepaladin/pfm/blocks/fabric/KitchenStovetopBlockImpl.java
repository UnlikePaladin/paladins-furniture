package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class KitchenStovetopBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return StovetopBlockEntity.getFactory().create(pos, state);
    }
}
