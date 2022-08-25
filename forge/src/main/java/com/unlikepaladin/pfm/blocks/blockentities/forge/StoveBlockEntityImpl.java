package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class StoveBlockEntityImpl extends StoveBlockEntity {

    public StoveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    //TODO: Sync Inventory Data to Client for Render
    public StoveBlockEntityImpl(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

}
