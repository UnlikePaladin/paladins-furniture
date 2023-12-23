package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class PFMBedBlockEntity extends BedBlockEntity implements DyeableFurnitureBlockEntity<PFMBedBlockEntity> {
    private final BlockEntityType<?> type;
    public PFMBedBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        super(pos, state, color);
        type = BlockEntities.BED_BLOCK_ENTITY;
    }

    public PFMBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        type = BlockEntities.BED_BLOCK_ENTITY;
    }

    @Override
    public void setPFMColor(DyeColor color) {
        this.setColor(color);
    }

    @Override
    public DyeColor getPFMColor() {
        return getColor();
    }

    @Override
    public NbtCompound writeColor(NbtCompound nbt) {
        nbt.putString("color", getColor().asString());
        return nbt;
    }

    @Override
    public PFMBedBlockEntity getEntity() {
        return this;
    }

    @Override
    public BlockEntityType<?> getType() {
        return type;
    }
}
