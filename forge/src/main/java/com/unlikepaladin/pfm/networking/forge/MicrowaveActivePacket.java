package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ForgePacketHandler;

import java.util.Objects;

public class MicrowaveActivePacket {
    private final BlockPos entityPos;
    private final boolean active;

    public MicrowaveActivePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    public static void handle(ForgePacketHandler forgePacketHandler, MicrowaveActivePacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Work that needs to be thread-safe (most work)
            ServerPlayer player = ctx.getSender(); // the client that sent this packet
            // Do stuffm
            BlockPos entityPos = msg.entityPos;
            boolean active = msg.active;
            Level world = Objects.requireNonNull(player).level();
            ctx.enqueueWork(() -> {
                if (world.hasChunkAt(entityPos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(entityPos);
                    microwaveBlockEntity.setActive(active);
                }
                else {
                    player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.setPacketHandled(true);
    }

    public static void encode(MicrowaveActivePacket packet, FriendlyByteBuf buffer) {
        BlockPos entityPos = packet.entityPos;
        boolean active = packet.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    public static MicrowaveActivePacket decode(FriendlyByteBuf buffer) {
            BlockPos entityPos = buffer.readBlockPos();
            boolean active = buffer.readBoolean();
            return new MicrowaveActivePacket(entityPos, active);
    }

}


