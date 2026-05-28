package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SinkBlockEntity extends BlockEntity {
    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SINK_BLOCK_ENTITY, pos, state);
    }
    private int sinkTimer = 0;
    private boolean isFilling = false;

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return super.getUpdateTag(registryLookup);
    }

    @Override
    protected void saveAdditional(WriteView view) {
        super.saveAdditional(view);
        view.putInt("sinkTimer", sinkTimer);
        view.putBoolean("isFilling", isFilling);
    }

    @Override
    protected void loadAdditional(ReadView view) {
        sinkTimer = view.getInt("sinkTimer", 0);
        isFilling = view.getBoolean("isFilling", false);
        super.loadAdditional(view);
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

    public static void tick(Level world, BlockPos pos, BlockState state, SinkBlockEntity blockEntity) {
        if (blockEntity.isFilling) {
            if (blockEntity.sinkTimer >= 30) {
                blockEntity.setSinkTimer(0);
                blockEntity.setFilling(false);
            } else {
                if (world.isClientSide) {
                    KitchenSinkBlock.spawnParticles(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), blockEntity.getLevel(), blockEntity.getBlockPos());
                }
                blockEntity.sinkTimer++;
            }
        }
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends SinkBlockEntity> getFactory() {
        throw new AssertionError();
    }
}
