package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BathtubBlockEntity extends BedBlockEntity implements TickableBlockEntity {
    public BathtubBlockEntity() {
        super(DyeColor.WHITE);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntities.BATHTUB_BLOCK_ENTITY;
    }

    private int fillTimer = 0;
    private boolean isFilling = false;
    @Override
    public CompoundTag getUpdateTag() {
        return this.writeIdentifyingTubData(new CompoundTag());
    }

    private CompoundTag writeIdentifyingTubData(CompoundTag nbt) {
        ResourceLocation identifier = BlockEntityType.getKey(this.getType());
        if (identifier == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        nbt.putString("id", identifier.toString());
        nbt.putInt("x", this.worldPosition.getX());
        nbt.putInt("y", this.worldPosition.getY());
        nbt.putInt("z", this.worldPosition.getZ());
        nbt.putInt("tubTimer", this.fillTimer);
        nbt.putBoolean("isTubFilling", this.isFilling);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        fillTimer = nbt.getInt("tubTimer");
        isFilling = nbt.getBoolean("isTubFilling");
        super.load(state, nbt);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("tubTimer", fillTimer);
        nbt.putBoolean("isTubFilling", isFilling);
        return nbt;
    }

    public void setFillTimer(int fillTimer) {
        this.fillTimer = fillTimer;
    }

    public void setFilling(boolean isFilling) {
        if (isFilling){
            level.playSound(null, worldPosition, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.7f, 1.0f);
        }
        this.isFilling = isFilling;
    }

    @Override
    public void tick() {
        if (this.isFilling) {
            if (this.fillTimer >= 30) {
                this.setFillTimer(0);
                this.setFilling(false);
            } else {
                if (level.isClientSide) {
                    BasicBathtubBlock.spawnParticles(this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), this.level, this.getBlockPos());
                }
                this.fillTimer++;
            }
        }
    }
}
