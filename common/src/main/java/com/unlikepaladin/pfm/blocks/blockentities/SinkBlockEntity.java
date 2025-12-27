package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.blocks.KitchenSinkBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SinkBlockEntity extends BlockEntity {
    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SINK_BLOCK_ENTITY, pos, state);
    }
    private int sinkTimer = 0;
    private boolean isFilling = false;

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return super.toInitialChunkDataNbt(registryLookup);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("sinkTimer", sinkTimer);
        view.putBoolean("isFilling", isFilling);
    }

    @Override
    protected void readData(ReadView view) {
        sinkTimer = view.getInt("sinkTimer", 0);
        isFilling = view.getBoolean("isFilling", false);
        super.readData(view);
    }

    public void setSinkTimer(int sinkTimer) {
        this.sinkTimer = sinkTimer;
    }

    public void setFilling(boolean isFilling) {
        if (isFilling){
            world.playSound(null, pos, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 0.7f, 1.0f);
        }
        this.isFilling = isFilling;
    }

    public static void tick(World world, BlockPos pos, BlockState state, SinkBlockEntity blockEntity) {
        if (blockEntity.isFilling) {
            if (blockEntity.sinkTimer >= 30) {
                blockEntity.setSinkTimer(0);
                blockEntity.setFilling(false);
            } else {
                if (world.isClient()) {
                    KitchenSinkBlock.spawnParticles(blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING), blockEntity.world, blockEntity.getPos());
                }
                blockEntity.sinkTimer++;
            }
        }
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntityFactory<? extends SinkBlockEntity> getFactory() {
        throw new AssertionError();
    }
}
