package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.stream.Stream;

public class MicrowaveBlockEntityImpl extends MicrowaveBlockEntity implements BlockEntityClientSerializable, ExtendedScreenHandlerFactory {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        Collection<ServerPlayerEntity> watchingPlayers = PlayerLookup.tracking(microwaveBlockEntity);
        // Look at the other methods of `PlayerStream` to capture different groups of players.
        // We'll get to this later
        PacketByteBuf clientData = new PacketByteBuf(Unpooled.buffer());
        clientData.writeBoolean(active);
        clientData.writeBlockPos(microwaveBlockEntity.getPos());
        // Then we'll send the packet to all the players
        watchingPlayers.forEach(player -> {
                    ServerPlayNetworking.send(player, NetworkIDs.MICROWAVE_UPDATE_PACKET_ID,clientData);
                }
        );
    }

    @Override
    public void fromClientTag(NbtCompound tag) {
        readNbt(tag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBoolean(this.isActive);
        packetByteBuf.writeBlockPos(this.pos);
    }
}
