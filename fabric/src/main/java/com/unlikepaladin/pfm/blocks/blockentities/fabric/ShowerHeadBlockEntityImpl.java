package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class ShowerHeadBlockEntityImpl extends ShowerHeadBlockEntity implements BlockEntityClientSerializable {
    public ShowerHeadBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return save(tag);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends ShowerHeadBlockEntity> getFactory() {
        return ShowerHeadBlockEntityImpl::new;
    }

}
