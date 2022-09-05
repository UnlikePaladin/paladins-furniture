package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.StoveBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class StoveImpl {

    public static BlockEntity getBlockEntity() {
        return new StoveBlockEntityImpl(BlockEntities.STOVE_BLOCK_ENTITY);
    }
}
