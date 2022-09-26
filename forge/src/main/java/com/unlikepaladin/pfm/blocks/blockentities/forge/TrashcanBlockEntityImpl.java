package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity {
    public TrashcanBlockEntityImpl(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity, BlockPos pos, BlockState state) {
        super(trashcanBlockEntity, pos, state);
    }

    public TrashcanBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }
}
