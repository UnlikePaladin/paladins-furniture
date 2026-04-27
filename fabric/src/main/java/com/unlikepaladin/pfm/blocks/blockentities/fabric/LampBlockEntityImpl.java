package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;

public class LampBlockEntityImpl extends LampBlockEntity implements BlockEntityClientSerializable {

    public LampBlockEntityImpl() {
        super();
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        load(this.getBlockState(), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return save(tag);
    }

    public static Supplier<? extends LampBlockEntity> getFactory() {
        return LampBlockEntityImpl::new;
    }
}
