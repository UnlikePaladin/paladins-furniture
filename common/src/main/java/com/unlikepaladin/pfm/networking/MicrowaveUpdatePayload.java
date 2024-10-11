package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import dev.architectury.networking.SpawnEntityPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public record MicrowaveUpdatePayload(BlockPos pos, Boolean isActive) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, MicrowaveUpdatePayload> PACKET_CODEC = CustomPayload.codecOf(MicrowaveUpdatePayload::write, MicrowaveUpdatePayload::new);
    public static final PacketCodec<PacketByteBuf, MicrowaveUpdatePayload> PACKET_SIMPLE_CODEC = CustomPayload.codecOf(MicrowaveUpdatePayload::write, MicrowaveUpdatePayload::new);

    public MicrowaveUpdatePayload(RegistryByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public MicrowaveUpdatePayload(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return NetworkIDs.MICROWAVE_UPDATE_PACKET_ID;
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void handle(PlayerEntity player, MinecraftClient client) {
        if (player.getWorld().isChunkLoaded(pos)) {
            client.execute(() -> {
                MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) player.getWorld().getBlockEntity(pos);
                if (Objects.nonNull(client.currentScreen) && client.currentScreen instanceof MicrowaveScreen currentScreen)  {
                    currentScreen.getScreenHandler().setActive(blockEntity, isActive);}
            });
        }
        else {
            client.player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}
