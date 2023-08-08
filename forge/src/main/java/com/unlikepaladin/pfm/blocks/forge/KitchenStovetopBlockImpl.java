package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class KitchenStovetopBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new StovetopBlockEntityImpl();
    }
}
