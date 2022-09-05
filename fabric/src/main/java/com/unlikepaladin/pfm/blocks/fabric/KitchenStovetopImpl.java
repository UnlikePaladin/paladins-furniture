package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.StovetopBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class KitchenStovetopImpl {
    public static BlockEntity getBlockEntity() {
        return new StovetopBlockEntityImpl();
    }
}
