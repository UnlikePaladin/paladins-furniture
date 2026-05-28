package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BathtubBlockEntity extends BedBlockEntity {
    public BathtubBlockEntity(BlockPos worldPosition, BlockState state) {
        super(worldPosition, state, DyeColor.WHITE);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntities.BATHTUB_BLOCK_ENTITY;
    }

    private int fillTimer = 0;
    private boolean isFilling = false;

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
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
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        fillTimer = nbt.getInt("tubTimer").orElse(0);
        isFilling = nbt.getBoolean("isTubFilling").orElse(false);
        super.loadAdditional(nbt, registryLookup);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putInt("tubTimer", fillTimer);
        nbt.putBoolean("isTubFilling", isFilling);
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

    public static void tick(Level level, BlockPos worldPosition, BlockState state, BathtubBlockEntity blockEntity) {
        if (blockEntity.isFilling) {
            if (blockEntity.fillTimer >= 30) {
                blockEntity.setFillTimer(0);
                blockEntity.setFilling(false);
            } else {
                if (level.isClientSide) {
                    BasicBathtubBlock.spawnParticles(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), blockEntity.level, blockEntity.getBlockPos());
                }
                blockEntity.fillTimer++;
            }
        }
    }

    @Override
    public boolean isValidBlockState(BlockState state) {
        return getType().isValid(state);
    }
}
