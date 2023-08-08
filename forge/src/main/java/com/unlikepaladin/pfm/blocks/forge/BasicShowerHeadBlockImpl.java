package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.PlateBlockEntityImpl;
import com.unlikepaladin.pfm.blocks.blockentities.forge.ShowerHeadBlockEntityImpl;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BasicShowerHeadBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new ShowerHeadBlockEntityImpl();
    }
}
