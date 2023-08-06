package com.unlikepaladin.pfm.blocks.blockentities;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;

public interface DyeableFurnitureBlockEntity <T extends BlockEntity> {
    void setPFMColor(DyeColor color);

    DyeColor getPFMColor();

    NbtCompound writeColor(NbtCompound nbt);

    T getEntity();
}
