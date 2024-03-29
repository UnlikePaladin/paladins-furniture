package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MicrowaveUpdatePacket {
    public final BlockPos entityPos;
    public final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    // In Packet class
    public static void handle(MicrowaveUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientMicrowaveUpdatePackeHandler.handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    public static void encode(MicrowaveUpdatePacket packet, PacketByteBuf buffer) {
        BlockPos entityPos = packet.entityPos;
        boolean active = packet.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    public static MicrowaveUpdatePacket decode(PacketByteBuf buffer) {
        BlockPos entityPos = buffer.readBlockPos();
        boolean active = buffer.readBoolean();
        return new MicrowaveUpdatePacket(entityPos, active);
    }

}
