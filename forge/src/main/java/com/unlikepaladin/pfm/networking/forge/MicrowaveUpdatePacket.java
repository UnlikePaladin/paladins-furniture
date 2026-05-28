package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.ForgePacketHandler;

public class MicrowaveUpdatePacket {
    public final BlockPos entityPos;
    public final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    // In Packet class
    public static void handle(ForgePacketHandler forgePacketHandler, MicrowaveUpdatePacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientMicrowaveUpdatePackeHandler.handlePacket(msg, ctx))
        );
        ctx.setPacketHandled(true);
    }

    public static void encode(MicrowaveUpdatePacket packet, FriendlyByteBuf buffer) {
        BlockPos entityPos = packet.entityPos;
        boolean active = packet.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    public static MicrowaveUpdatePacket decode(FriendlyByteBuf buffer) {
        BlockPos entityPos = buffer.readBlockPos();
        boolean active = buffer.readBoolean();
        return new MicrowaveUpdatePacket(entityPos, active);
    }

}
