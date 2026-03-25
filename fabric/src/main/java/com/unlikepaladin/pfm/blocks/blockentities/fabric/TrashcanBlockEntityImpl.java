package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity implements ExtendedScreenHandlerFactory, BlockEntityClientSerializable {
    public TrashcanBlockEntityImpl(BlockPos pos, BlockState state) {
        super(BlockEntities.TRASHCAN_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer serverPlayerEntity, FriendlyByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.worldPosition);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return save(tag);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends TrashcanBlockEntity> getFactory() {
        return TrashcanBlockEntityImpl::new;
    }
}
