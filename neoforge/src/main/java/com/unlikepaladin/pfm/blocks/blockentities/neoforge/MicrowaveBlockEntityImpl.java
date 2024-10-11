package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.MicrowaveUpdatePayload;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MicrowaveBlockEntityImpl  extends MicrowaveBlockEntity {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        BlockPos pos = microwaveBlockEntity.getPos();
        WorldChunk chunk = Objects.requireNonNull(microwaveBlockEntity.getWorld()).getWorldChunk(pos);
        PacketDistributor.sendToPlayersTrackingChunk((ServerWorld) microwaveBlockEntity.getWorld(), chunk.getPos(), new MicrowaveUpdatePayload(pos, active));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);
        nbt.putBoolean("isActive", this.isActive);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
        return nbt;
    }


    @Override
    public void handleUpdateTag(NbtCompound tag, RegistryWrapper.WrapperLookup lookupProvider) {
        this.readNbt(tag, lookupProvider);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt, RegistryWrapper.WrapperLookup lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        this.isActive = pkt.getNbt().getBoolean("isActive");
        Inventories.readNbt(pkt.getNbt(), this.inventory, lookupProvider);
    }

    public static BlockEntityType.BlockEntityFactory<? extends MicrowaveBlockEntity> getFactory() {
        return MicrowaveBlockEntityImpl::new;
    }
}
