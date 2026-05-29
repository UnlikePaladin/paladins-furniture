package com.unlikepaladin.pfm.blocks.blockentities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public interface DyeableFurnitureBlockEntity <T extends BlockEntity> {
    void setPFMColor(DyeColor color);

    DyeColor getPFMColor();

    CompoundTag writeColor(CompoundTag nbt);

    T getEntity();
}
