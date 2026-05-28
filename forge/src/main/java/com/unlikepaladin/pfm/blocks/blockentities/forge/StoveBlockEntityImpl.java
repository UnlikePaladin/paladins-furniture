package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

public class StoveBlockEntityImpl extends StoveBlockEntity {

    public StoveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public StoveBlockEntityImpl(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    @Override
    public void handleUpdateTag(ReadView tag, HolderLookup.Provider holders) {
        this.loadAdditional(tag, holders);
        super.handleUpdateTag(tag, holders);
    }

    @Override
    public void onDataPacket(ClientConnection connection, ReadView data, RegistryWrapper.WrapperLookup lookup) {
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(data, this.itemsBeingCooked, lookup);
        super.onDataPacket(connection, data, lookup);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends BlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? StoveBlockEntityBalm::new :StoveBlockEntityImpl::new;
    }
}
