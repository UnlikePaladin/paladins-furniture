package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public record MicrowaveUpdatePayload(BlockPos pos, Boolean isActive) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, MicrowaveUpdatePayload> PACKET_CODEC = CustomPacketPayload.codec(MicrowaveUpdatePayload::write, MicrowaveUpdatePayload::new);
    public static final StreamCodec<FriendlyByteBuf, MicrowaveUpdatePayload> PACKET_SIMPLE_CODEC = CustomPacketPayload.codec(MicrowaveUpdatePayload::write, MicrowaveUpdatePayload::new);

    public MicrowaveUpdatePayload(RegistryFriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    public MicrowaveUpdatePayload(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkIDs.MICROWAVE_UPDATE_PACKET_ID;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(isActive);
    }

    public void handle(Player player, Minecraft client) {
        if (player.level().hasChunkAt(pos)) {
            client.execute(() -> {
                MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) player.level().getBlockEntity(pos);
                if (Objects.nonNull(client.screen) && client.screen instanceof MicrowaveScreen currentScreen)  {
                    currentScreen.getMenu().setActive(blockEntity, isActive);}
            });
        }
        else {
            client.player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}
