package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.MicrowaveUpdatePayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MicrowaveBlockEntityImpl extends MicrowaveBlockEntity {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        BlockPos pos = microwaveBlockEntity.getBlockPos();
        LevelChunk chunk = Objects.requireNonNull(microwaveBlockEntity.getLevel()).getChunkAt(pos);
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) microwaveBlockEntity.getLevel(), chunk.getPos(), new MicrowaveUpdatePayload(pos, active));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public void handleUpdateTag(ReadView input) {
        this.readData(input);
    }

    @Override
    public void onDataPacket(Connection net, ReadView valueInput) {
        super.onDataPacket(net, pkt, lookupProvider);
        this.container = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.isActive = valueInput.getBoolean("isActive", false);
        ContainerHelper.loadAllItems(valueInputff, this.container, lookupProvider);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends MicrowaveBlockEntity> getFactory() {
        return MicrowaveBlockEntityImpl::new;
    }
}
