package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StovetopBlockEntityImpl extends StovetopBlockEntity {
    public StovetopBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ReadView tag, HolderLookup.Provider holders) {
        this.loadAdditional(tag, holders);
    }

    @Override
    public void onDataPacket(Connection connection, ReadView data, RegistryWrapper.WrapperLookup lookup) {
        super.onDataPacket(connection, data, lookup);
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(pkt.getTag(), this.itemsBeingCooked);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends StovetopBlockEntity> getFactory() {
        return StovetopBlockEntityImpl::new;
    }
}
