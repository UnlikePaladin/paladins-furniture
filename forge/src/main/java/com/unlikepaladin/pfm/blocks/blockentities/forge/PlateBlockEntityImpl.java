package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

public class PlateBlockEntityImpl extends PlateBlockEntity {
    public PlateBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }

    @Override
    public void handleUpdateTag(ValueInput tag, HolderLookup.Provider holders) {
        super.handleUpdateTag(tag, holders);
        this.loadAdditional(tag);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput data, HolderLookup.Provider registryLookup) {
        super.onDataPacket(net, data, registryLookup);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends PlateBlockEntity> getFactory() {
        return PlateBlockEntityImpl::new;
    }
}
