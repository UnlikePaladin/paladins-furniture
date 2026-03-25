package com.unlikepaladin.pfm.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public interface DyeableFurnitureEntity<T extends Entity> {
    void setPFMColor(DyeColor color);

    DyeColor getPFMColor();

    CompoundTag writeColor(CompoundTag nbt);

    T getEntity();
}
