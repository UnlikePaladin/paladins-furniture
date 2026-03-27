package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;;
import java.util.Collection;
import java.util.stream.Stream;

public class MicrowaveBlockEntityImpl extends MicrowaveBlockEntity implements ExtendedScreenHandlerFactory {
    public MicrowaveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static void setActiveonClient(MicrowaveBlockEntity microwaveBlockEntity, boolean active) {
        microwaveBlockEntity.setActive(active);
        Collection<ServerPlayer> watchingPlayers = PlayerLookup.tracking(microwaveBlockEntity);
        // Look at the other methods of `PlayerStream` to capture different groups of players.
        // We'll get to this later
        FriendlyByteBuf clientData = new FriendlyByteBuf(Unpooled.buffer());
        clientData.writeBoolean(active);
        clientData.writeBlockPos(microwaveBlockEntity.getBlockPos());
        // Then we'll send the packet to all the players
        watchingPlayers.forEach(player -> {
                    ServerPlayNetworking.send(player, NetworkIDs.MICROWAVE_UPDATE_PACKET_ID,clientData);
                }
        );
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket toUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    @Override
    public void writeScreenOpeningData(ServerPlayer serverPlayerEntity, FriendlyByteBuf packetByteBuf) {
        packetByteBuf.writeBoolean(this.isActive);
        packetByteBuf.writeBlockPos(this.worldPosition);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends MicrowaveBlockEntity> getFactory() {
        return MicrowaveBlockEntityImpl::new;
    }
}
