package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class KitchenStovetopBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new StovetopBlockEntityImpl();
    }
}
