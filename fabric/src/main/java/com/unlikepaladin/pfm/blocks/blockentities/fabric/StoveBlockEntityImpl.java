package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Supplier;

public class StoveBlockEntityImpl extends StoveBlockEntity implements ExtendedScreenHandlerFactory, BlockEntityClientSerializable {

    public StoveBlockEntityImpl() {
        super();
    }

    public StoveBlockEntityImpl(BlockEntityType<?> entity) {
        super(entity);
    }

    public void fromClientTag(CompoundTag tag) {
        load(getBlockState(), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return save(tag);
    }

    public static Supplier<? extends BlockEntity> getFactory() {
        return StoveBlockEntityImpl::new;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer serverPlayerEntity, FriendlyByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.worldPosition);
    }
}
