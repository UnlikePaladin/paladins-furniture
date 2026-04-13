package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShowerHeadBlockEntityImpl extends ShowerHeadBlockEntity {
    public ShowerHeadBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveInitialChunkData(new CompoundTag(), registryLookup);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider holders) {
        this.loadAdditional(tag, holders);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
        super.onDataPacket(net, pkt, lookup);
        this.isOpen = pkt.getTag().getBoolean("isOpen");
    }

    public static BlockEntityType.BlockEntitySupplier<? extends ShowerHeadBlockEntity> getFactory() {
        return ShowerHeadBlockEntityImpl::new;
    }
}
