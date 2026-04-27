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
import net.minecraftforge.fml.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class MicrowaveBlockEntityImpl extends MicrowaveBlockEntity {
    public MicrowaveBlockEntityImpl() {
        super();
    }
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        BlockPos pos = microwaveBlockEntity.getBlockPos();
        LevelChunk chunk = Objects.requireNonNull(microwaveBlockEntity.getLevel()).getChunkAt(pos);
        NetworkRegistryForge.PFM_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MicrowaveUpdatePacket(pos, active));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 13, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("isActive", this.isActive);
        ContainerHelper.saveAllItems(nbt, this.container);
        return nbt;
    }


    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag) {
        this.fromTag(state,tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.container = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.isActive = pkt.getTag().getBoolean("isActive");
        ContainerHelper.loadAllItems(pkt.getTag(), this.container);
    }

    public static Supplier<? extends MicrowaveBlockEntity> getFactory() {
        return MicrowaveBlockEntityImpl::new;
    }
}
