package com.unlikepaladin.pfm.networking.neoforge;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MicrowaveUpdatePacket {
    public final BlockPos entityPos;
    public final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    // In Packet class
    public static void handle(MicrowaveUpdatePacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() ->
                // Make sure it's only executed on the physical client
                {
                    if (FMLEnvironment.dist.isClient()) {
                        ClientMicrowaveUpdatePackeHandler.handlePacket(msg, ctx);
                    }
                }
        );
        ctx.setPacketHandled(true);
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
