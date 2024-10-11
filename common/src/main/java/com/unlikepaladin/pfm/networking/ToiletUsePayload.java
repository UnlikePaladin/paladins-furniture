package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record ToiletUsePayload(BlockPos pos) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, ToiletUsePayload> PACKET_CODEC = CustomPayload.codecOf(ToiletUsePayload::write, ToiletUsePayload::new);
    public static final PacketCodec<PacketByteBuf, ToiletUsePayload> PACKET_SIMPLE_CODEC = CustomPayload.codecOf(ToiletUsePayload::write, ToiletUsePayload::new);

    public ToiletUsePayload(RegistryByteBuf registryByteBuf) {
        this(registryByteBuf.readBlockPos());
    }

    public ToiletUsePayload(PacketByteBuf registryByteBuf) {
        this(registryByteBuf.readBlockPos());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return NetworkIDs.TOILET_USE_ID;
    }

    public void write(RegistryByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayerEntity player) {
        // Get the BlockPos we put earlier, in the networking thread
        BlockPos blockPos = pos();
        server.submitAndJoin(() -> {
            // Use the pos in the main thread
            World world = player.getWorld();
            if (world.isChunkLoaded(blockPos)) {
                world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
            } else {
                player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
            }
        });
    }
}
