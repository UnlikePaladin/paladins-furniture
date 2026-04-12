package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity implements ExtendedScreenHandlerFactory<TrashcanScreenHandler.TrashCanData> {
    public TrashcanBlockEntityImpl(BlockPos pos, BlockState state) {
        super(BlockEntities.TRASHCAN_BLOCK_ENTITY, pos, state);
    }

    @Override
    public TrashcanScreenHandler.TrashCanData getScreenOpeningData(ServerPlayer player) {
        return new TrashcanScreenHandler.TrashCanData(this.getBlockPos());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends TrashcanBlockEntity> getFactory() {
        return TrashcanBlockEntityImpl::new;
    }
}
