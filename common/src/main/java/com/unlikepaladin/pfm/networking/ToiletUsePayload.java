package com.unlikepaladin.pfm.networking;

import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public record ToiletUsePayload(BlockPos pos) implements CustomPacketPayload {
    public static final StreamCodec<RegistryFriendlyByteBuf, ToiletUsePayload> PACKET_CODEC = CustomPacketPayload.codec(ToiletUsePayload::write, ToiletUsePayload::new);
    public static final StreamCodec<FriendlyByteBuf, ToiletUsePayload> PACKET_SIMPLE_CODEC = CustomPacketPayload.codec(ToiletUsePayload::write, ToiletUsePayload::new);

    public ToiletUsePayload(RegistryFriendlyByteBuf registryByteBuf) {
        this(registryByteBuf.readBlockPos());
    }

    public ToiletUsePayload(FriendlyByteBuf registryByteBuf) {
        this(registryByteBuf.readBlockPos());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkIDs.TOILET_USE_ID;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        // Get the BlockPos we put earlier, in the networking thread
        BlockPos blockPos = pos();
        server.executeBlocking(() -> {
            // Use the pos in the main thread
            Level world = player.level();
            if (world.hasChunkAt(blockPos)) {
                world.setBlockAndUpdate(blockPos, world.getBlockState(blockPos).setValue(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundSource.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
            } else {
                player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
            }
        });
    }
}
