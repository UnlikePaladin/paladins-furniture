package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public record TrashcanClearPayload(BlockPos pos) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, TrashcanClearPayload> PACKET_CODEC = CustomPayload.codecOf(TrashcanClearPayload::write, TrashcanClearPayload::new);
    public static final PacketCodec<PacketByteBuf, TrashcanClearPayload> PACKET_SIMPLE_CODEC = CustomPayload.codecOf(TrashcanClearPayload::write, TrashcanClearPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return NetworkIDs.TRASHCAN_CLEAR;
    }

    public TrashcanClearPayload(RegistryByteBuf buf) {
        this(buf.readBlockPos());
    }

    public TrashcanClearPayload(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayerEntity player) {
        server.submitAndJoin(() -> {
            if(Objects.nonNull(player.getWorld().getBlockEntity(pos))){
                World world = player.getWorld();
                if (world.isChunkLoaded(pos)) {
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(pos);
                    trashcanBlockEntity.clear();
                } else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }
}
