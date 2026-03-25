package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class TrashcanClearPacket {
    private final BlockPos blockPos;
    public TrashcanClearPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void handle(TrashcanClearPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            BlockPos entityPos = msg.blockPos;
            Level world = Objects.requireNonNull(player).getCommandSenderWorld();
            ctx.get().enqueueWork(() -> {
                if (world.hasChunkAt(entityPos)) {
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) player.level.getBlockEntity(entityPos);
                    trashcanBlockEntity.clearContent();
                }
                else {
                    player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }


    public static void encode(TrashcanClearPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static TrashcanClearPacket decode(FriendlyByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        return new TrashcanClearPacket(blockPos);
    }
}
