package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class StovetopBlockEntityImpl extends StovetopBlockEntity {
    public StovetopBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    //TODO: Sync Inventory Data to Client for Render
}
