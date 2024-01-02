package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class TrashcanClearPacket {
    private final BlockPos blockPos;
    public TrashcanClearPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void handle(TrashcanClearPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            BlockPos entityPos = msg.blockPos;
            World world = Objects.requireNonNull(player).getEntityWorld();
            ctx.enqueueWork(() -> {
                if (world.isChunkLoaded(entityPos)) {
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(entityPos);
                    trashcanBlockEntity.clear();
                }
                else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.setPacketHandled(true);
    }


    public static void encode(TrashcanClearPacket packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static TrashcanClearPacket decode(PacketByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        return new TrashcanClearPacket(blockPos);
    }
}
