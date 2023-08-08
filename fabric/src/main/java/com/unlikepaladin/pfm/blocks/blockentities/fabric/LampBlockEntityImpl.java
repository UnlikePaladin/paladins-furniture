package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class LampBlockEntityImpl extends LampBlockEntity implements BlockEntityClientSerializable {

    public LampBlockEntityImpl() {
        super();
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        fromTag(this.getCachedState(), tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }

    public static Supplier<? extends LampBlockEntity> getFactory() {
        return LampBlockEntityImpl::new;
    }
}
