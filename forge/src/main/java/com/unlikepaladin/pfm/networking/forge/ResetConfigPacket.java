package com.unlikepaladin.pfm.networking.forge;

import net.minecraft.network.PacketByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetConfigPacket {
    public ResetConfigPacket() {
    }

    public static void handle(ResetConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientResetConfigPacketHandler.handlePacket(msg, ctx)));
        ctx.get().setPacketHandled(true);
    }

    public static void encode(ResetConfigPacket packet, PacketByteBuf buffer) {

    }

    public static ResetConfigPacket decode(PacketByteBuf buffer) {
        return new ResetConfigPacket();
    }
}
