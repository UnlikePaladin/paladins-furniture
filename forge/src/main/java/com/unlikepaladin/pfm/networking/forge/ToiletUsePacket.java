package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ForgePacketHandler;

import java.util.Objects;
import java.util.function.Supplier;

public class ToiletUsePacket {
    private final BlockPos blockPos;
    public ToiletUsePacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void handle(ForgePacketHandler forgePacketHandler, ToiletUsePacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender(); // the client that sent this packet

            BlockPos blockPos = msg.blockPos;
            Level world = Objects.requireNonNull(player).level();
            ctx.enqueueWork(() -> {
                if (world.hasChunkAt(blockPos)) {
                    world.setBlockAndUpdate(blockPos, world.getBlockState(blockPos).setValue(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundSource.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                }
                else {
                    player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.setPacketHandled(true);
    }

    public static void encode(ToiletUsePacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static ToiletUsePacket decode(FriendlyByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        return new ToiletUsePacket(blockPos);
    }

}


