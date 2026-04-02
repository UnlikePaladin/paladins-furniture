package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;;

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
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = this.saveInitialChunkData(new CompoundTag());
        ContainerHelper.saveAllItems(nbt, this.itemInPlate, true);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.itemInPlate.clear();
        ContainerHelper.loadAllItems(pkt.getTag(), this.itemInPlate);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends PlateBlockEntity> getFactory() {
        return PlateBlockEntityImpl::new;
    }
}
