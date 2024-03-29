package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MicrowaveActivePacket {
    private final BlockPos entityPos;
    private final boolean active;

    public MicrowaveActivePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    public static void handle(MicrowaveActivePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Work that needs to be thread-safe (most work)
            ServerPlayerEntity player = ctx.get().getSender(); // the client that sent this packet
            // Do stuffm
            BlockPos entityPos = msg.entityPos;
            boolean active = msg.active;
            World world = Objects.requireNonNull(player).getEntityWorld();
            ctx.get().enqueueWork(() -> {
                if (world.isChunkLoaded(entityPos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.world.getBlockEntity(entityPos);
                    microwaveBlockEntity.setActive(active);
                }
                else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void encode(MicrowaveActivePacket packet, PacketByteBuf buffer) {
        BlockPos entityPos = packet.entityPos;
        boolean active = packet.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    public static MicrowaveActivePacket decode(PacketByteBuf buffer) {
            BlockPos entityPos = buffer.readBlockPos();
            boolean active = buffer.readBoolean();
            return new MicrowaveActivePacket(entityPos, active);
    }

}


