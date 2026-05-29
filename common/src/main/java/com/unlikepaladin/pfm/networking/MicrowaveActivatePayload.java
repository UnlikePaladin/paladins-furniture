package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public record MicrowaveActivatePayload(BlockPos pos, Boolean isActive) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, MicrowaveActivatePayload> PACKET_CODEC = CustomPacketPayload.codec(MicrowaveActivatePayload::write, MicrowaveActivatePayload::new);
    public static final StreamCodec<FriendlyByteBuf, MicrowaveActivatePayload> PACKET_SIMPLE_CODEC = CustomPacketPayload.codec(MicrowaveActivatePayload::write, MicrowaveActivatePayload::new);

    public MicrowaveActivatePayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public MicrowaveActivatePayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.executeBlocking(() -> {
            if(Objects.nonNull(player.level().getBlockEntity(pos))){
                Level world = player.level();
                if (world.hasChunkAt(pos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(isActive);
                } else {
                    player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID;
    }
}
