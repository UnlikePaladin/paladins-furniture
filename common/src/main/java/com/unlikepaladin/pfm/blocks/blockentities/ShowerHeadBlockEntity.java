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
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Random;
import java.util.function.Supplier;

public class ShowerHeadBlockEntity extends BlockEntity implements TickableBlockEntity {
    public ShowerHeadBlockEntity() {
        super(BlockEntities.SHOWER_HEAD_BLOCK_ENTITY);
    }
    protected boolean isOpen = false;
    @Override
    public CompoundTag getUpdateTag() {
        return super.getUpdateTag();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean("isOpen", isOpen);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        isOpen = nbt.getBoolean("isOpen");
        super.load(state, nbt);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void tick() {
        if (level != null && this.isOpen && level.isClientSide) {
            spawnParticles(this.getBlockState().getValue( BlockStateProperties.HORIZONTAL_FACING), this.level, this.getBlockPos());
        }
        if (this.isOpen) {
            level.playSound(null, worldPosition, SoundEvents.WEATHER_RAIN, SoundSource.BLOCKS, 0.1f, 8.0f);
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
        Random rand = world.random;
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
        super.save(nbt);
        nbt.putBoolean("isOpen", isOpen);
        return nbt;
    }

    @ExpectPlatform
    public static Supplier<? extends ShowerHeadBlockEntity> getFactory() {
        throw new UnsupportedOperationException();
    }
}
