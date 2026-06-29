package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.StoveData;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.StoveBlockEntityBalm;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

;

public class StoveBlockEntityImpl extends StoveBlockEntity implements ExtendedScreenHandlerFactory<StoveData> {

    public StoveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public StoveBlockEntityImpl(BlockEntityType<? extends OvenBlockEntity> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends OvenBlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? StoveBlockEntityBalm::new :StoveBlockEntityImpl::new;
    }

    @Override
    public StoveData getScreenOpeningData(ServerPlayer serverPlayerEntity) {
        return new StoveData(this.getBlockPos());
    }
}
