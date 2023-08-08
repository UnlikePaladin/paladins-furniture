package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class ShowerHeadBlockEntityImpl extends ShowerHeadBlockEntity implements BlockEntityClientSerializable {
    public ShowerHeadBlockEntityImpl() {
        super();
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        fromTag(getCachedState(), tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }

    public static Supplier<? extends ShowerHeadBlockEntity> getFactory() {
        return ShowerHeadBlockEntityImpl::new;
    }

}
