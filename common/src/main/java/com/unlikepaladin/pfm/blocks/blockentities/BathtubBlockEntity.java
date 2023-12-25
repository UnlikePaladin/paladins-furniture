package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.BasicBathtubBlock;
import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BathtubBlockEntity extends BedBlockEntity implements Tickable {
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
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeIdentifyingTubData(new NbtCompound());
    }

    private NbtCompound writeIdentifyingTubData(NbtCompound nbt) {
        Identifier identifier = BlockEntityType.getId(this.getType());
        if (identifier == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        nbt.putString("id", identifier.toString());
        nbt.putInt("x", this.pos.getX());
        nbt.putInt("y", this.pos.getY());
        nbt.putInt("z", this.pos.getZ());
        nbt.putInt("tubTimer", this.fillTimer);
        nbt.putBoolean("isTubFilling", this.isFilling);
        return nbt;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound nbt) {
        fillTimer = nbt.getInt("tubTimer");
        isFilling = nbt.getBoolean("isTubFilling");
        super.fromTag(state, nbt);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("tubTimer", fillTimer);
        nbt.putBoolean("isTubFilling", isFilling);
        return nbt;
    }

    public void setFillTimer(int fillTimer) {
        this.fillTimer = fillTimer;
    }

    public void setFilling(boolean isFilling) {
        if (isFilling){
            world.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 0.7f, 1.0f);
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
                if (world.isClient) {
                    BasicBathtubBlock.spawnParticles(this.getCachedState().get(Properties.HORIZONTAL_FACING), this.world, this.getPos());
                }
                this.fillTimer++;
            }
        }
    }
}
