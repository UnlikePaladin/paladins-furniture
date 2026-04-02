package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.forge.MicrowaveUpdatePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;
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
        NetworkRegistryForge.PFM_CHANNEL.send(new MicrowaveUpdatePacket(pos, active), PacketDistributor.TRACKING_CHUNK.with(chunk));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("isActive", this.isActive);
        ContainerHelper.saveAllItems(nbt, this.container);
        return nbt;
    }


    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.container = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.isActive = pkt.getTag().getBoolean("isActive");
        ContainerHelper.loadAllItems(pkt.getTag(), this.container);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends MicrowaveBlockEntity> getFactory() {
        return MicrowaveBlockEntityImpl::new;
    }
}
