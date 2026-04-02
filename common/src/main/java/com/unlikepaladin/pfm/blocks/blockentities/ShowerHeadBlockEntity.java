package com.unlikepaladin.pfm.blocks.blockentities;

import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.ParticleIDs;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class ShowerHeadBlockEntity extends BlockEntity {
    public ShowerHeadBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.SHOWER_HEAD_BLOCK_ENTITY, pos, state);
    }
    protected boolean isOpen = false;
    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("isOpen", isOpen);
    }

    @Override
    public void load(CompoundTag nbt) {
        isOpen = nbt.getBoolean("isOpen");
        super.load(nbt);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ShowerHeadBlockEntity blockEntity) {
        if (blockEntity.isOpen && world.isClientSide) {
            spawnParticles(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), blockEntity.getLevel(), blockEntity.getBlockPos());
        }
        if (blockEntity.isOpen) {
            world.playSound(null, pos, SoundEvents.WEATHER_RAIN, SoundSource.BLOCKS, 0.1f, 8.0f);
        }
    }

    public static void spawnParticles(Direction facing, Level world, BlockPos pos) {
        if (world.isClientSide) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            if (facing == Direction.WEST) {
                addShowerParticles(world, pos, new float[]{0.55f, 0.2f, 0.5f}, new float[]{0.1f, 0f, 0.1f});
            }
            else if (facing == Direction.NORTH){
                addShowerParticles(world, pos, new float[]{0.5f, 0.2f, 0.55f}, new float[]{0.1f, 0f, 0.1f});
            }
            else if (facing == Direction.SOUTH){
                addShowerParticles(world, pos, new float[]{0.5f, 0.2f, 0.45f}, new float[]{0.1f, 0f, 0.1f});
            }
            else {
                addShowerParticles(world, pos, new float[]{0.45f, 0.2f, 0.5f}, new float[]{0.1f, 0f, 0.1f});
            }
        }
    }

    public static void addShowerParticles(Level world, BlockPos pos, float[] offset, float[] difference) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        RandomSource rand = world.random;
        if (rand.nextBoolean()) {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] - difference[0]), y + (offset[1] - difference[1]), z + (offset[2]), 0.0, 0.0, 0.0);
        } else {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] - difference[0]), y + (offset[1] - difference[1]), z + (offset[2] - difference[2]), 0.0, 0.0, 0.0);
        }

        if (rand.nextBoolean()) {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] - difference[0]), y + (offset[1] - difference[1]), z + (offset[2] + difference[2]), 0.0, 0.0, 0.0);
        } else {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0]), y + (offset[1] - difference[1]), z + (offset[2]), 0.0, 0.0, 0.0);
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0]), y + (offset[1] - difference[1]), z + (offset[2] - difference[2]), 0.0, 0.0, 0.0);
       }

        if (rand.nextBoolean()) {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0]), y + (offset[1] - difference[1]), z + (offset[2] + difference[2]), 0.0, 0.0, 0.0);
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] + difference[0]), y + (offset[1] - difference[1]), z + (offset[2]), 0.0, 0.0, 0.0);
        } else {
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] + difference[0]), y + (offset[1] - difference[1]), z + (offset[2] - difference[2]), 0.0, 0.0, 0.0);
            world.addParticle(ParticleIDs.WATER_DROP, true, x + (offset[0] + difference[0]), y + (offset[1] - difference[1]), z + (offset[2] + difference[2]), 0.0, 0.0, 0.0);
        }
    }

    protected CompoundTag saveInitialChunkData(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("isOpen", isOpen);
        return nbt;
    }

    @ExpectPlatform
    public static BlockEntityType.BlockEntitySupplier<? extends ShowerHeadBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
