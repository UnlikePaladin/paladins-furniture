package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.Optional;

public class ToiletUsePacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_use");
    private final BlockPos blockPos;
    public ToiletUsePacket(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }

    public ToiletUsePacket(BlockPos pos) {
        this.blockPos = pos;
    }

    public static void handle(ToiletUsePacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            Optional<PlayerEntity> optionalPlayerEntity = ctx.player();
            if (optionalPlayerEntity.isPresent()) {
                PlayerEntity player = optionalPlayerEntity.get();
                BlockPos blockPos = msg.blockPos;
                Level world = player.level();
                if (world.hasChunkAt(blockPos)) {
                    world.setBlockAndUpdate(blockPos, world.getBlockState(blockPos).setValue(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                    world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundSource.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                }
                else {
                    player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
