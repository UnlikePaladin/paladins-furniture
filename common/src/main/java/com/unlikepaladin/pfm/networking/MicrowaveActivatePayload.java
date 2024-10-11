package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
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

public record MicrowaveActivatePayload(BlockPos pos, Boolean isActive) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, MicrowaveActivatePayload> PACKET_CODEC = CustomPayload.codecOf(MicrowaveActivatePayload::write, MicrowaveActivatePayload::new);
    public static final PacketCodec<PacketByteBuf, MicrowaveActivatePayload> PACKET_SIMPLE_CODEC = CustomPayload.codecOf(MicrowaveActivatePayload::write, MicrowaveActivatePayload::new);

    public MicrowaveActivatePayload(RegistryByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public MicrowaveActivatePayload(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void handle(MinecraftServer server, ServerPlayerEntity player) {
        server.submitAndJoin(() -> {
            if(Objects.nonNull(player.getWorld().getBlockEntity(pos))){
                World world = player.getWorld();
                if (world.isChunkLoaded(pos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(isActive);
                } else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID;
    }
}
