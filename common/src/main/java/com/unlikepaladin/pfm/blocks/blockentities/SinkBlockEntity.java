package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.AbstractSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

import java.util.function.Supplier;

public class SinkBlockEntity extends BlockEntity implements TickableBlockEntity {
    public SinkBlockEntity() {
        super(BlockEntities.SINK_BLOCK_ENTITY);
    }
    private int sinkTimer = 0;
    private boolean isFilling = false;
    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt("sinkTimer", sinkTimer);
        nbt.putBoolean("isFilling", isFilling);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        sinkTimer = nbt.getInt("sinkTimer");
        isFilling = nbt.getBoolean("isFilling");
        super.load(state, nbt);
    }

    public void setSinkTimer(int sinkTimer) {
        this.sinkTimer = sinkTimer;
    }

    public void setFilling(boolean isFilling) {
        if (isFilling){
            level.playSound(null, getBlockPos(), SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.7f, 1.0f);
        }
        this.isFilling = isFilling;
    }

    @Override
    public void tick() {
        if (this.isFilling) {
            if (this.sinkTimer >= 30) {
                this.setSinkTimer(0);
                this.setFilling(false);
            } else {
                if (level.isClientSide) {
                    AbstractSinkBlock.spawnParticles(this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), this.getLevel(), this.getBlockPos());
                }
                this.sinkTimer++;
            }
        }
    }

    @ExpectPlatform
    public static Supplier<? extends SinkBlockEntity> getFactory() {
        throw new AssertionError();
    }
}
