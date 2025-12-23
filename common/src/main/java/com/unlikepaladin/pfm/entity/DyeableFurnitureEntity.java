package com.unlikepaladin.pfm.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;

public interface DyeableFurnitureEntity<T extends Entity> {
    void setPFMColor(DyeColor color);

    DyeColor getPFMColor();

    NbtCompound writeColor(NbtCompound nbt);

    T getEntity();
}
