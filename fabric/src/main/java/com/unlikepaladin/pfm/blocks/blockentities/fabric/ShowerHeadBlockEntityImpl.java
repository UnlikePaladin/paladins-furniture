package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class ShowerHeadBlockEntityImpl extends ShowerHeadBlockEntity implements BlockEntityClientSerializable {
    public ShowerHeadBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }

}
