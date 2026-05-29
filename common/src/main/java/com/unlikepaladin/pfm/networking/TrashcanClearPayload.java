package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
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

public record TrashcanClearPayload(BlockPos pos) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, TrashcanClearPayload> PACKET_CODEC = CustomPacketPayload.codec(TrashcanClearPayload::write, TrashcanClearPayload::new);
    public static final StreamCodec<FriendlyByteBuf, TrashcanClearPayload> PACKET_SIMPLE_CODEC = CustomPacketPayload.codec(TrashcanClearPayload::write, TrashcanClearPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkIDs.TRASHCAN_CLEAR;
    }

    public TrashcanClearPayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public TrashcanClearPayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.executeBlocking(() -> {
            if(Objects.nonNull(player.level().getBlockEntity(pos))){
                Level world = player.level();
                if (world.hasChunkAt(pos)) {
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(pos);
                    trashcanBlockEntity.clearContent();
                } else {
                    player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }
}
