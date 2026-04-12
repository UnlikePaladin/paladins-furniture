package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;

public class PFMBedBlockEntity extends BedBlockEntity implements DyeableFurnitureBlockEntity<PFMBedBlockEntity> {
    public PFMBedBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        super(pos, state, color);
    }

    public PFMBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
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
    public CompoundTag writeColor(CompoundTag nbt) {
        nbt.putString("color", getColor().getSerializedName());
        return nbt;
    }

    @Override
    public PFMBedBlockEntity getEntity() {
        return this;
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntities.BED_BLOCK_ENTITY;
    }

    @Override
    public boolean supports(BlockState state) {
        return getType().supports(state);
    }
}
