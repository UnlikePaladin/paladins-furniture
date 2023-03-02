package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ToiletUsePacket {
    private final BlockPos blockPos;
    public ToiletUsePacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void handle(ToiletUsePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender(); // the client that sent this packet

            BlockPos blockPos = msg.blockPos;
            World world = Objects.requireNonNull(player).getEntityWorld();
            ctx.get().enqueueWork(() -> {
                if (world.isChunkLoaded(blockPos)) {
                    world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                }
                else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void encode(ToiletUsePacket packet, PacketByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static ToiletUsePacket decode(PacketByteBuf buffer) {
        BlockPos blockPos = buffer.readBlockPos();
        return new ToiletUsePacket(blockPos);
    }

}


