package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.Optional;

public class TrashcanClearPacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "trashcan_clear");
    private final BlockPos blockPos;

    public TrashcanClearPacket(PacketByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public TrashcanClearPacket(BlockPos pos) {
        this.blockPos = pos;
    }

    public static void handle(TrashcanClearPacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            Optional<PlayerEntity> optionalPlayerEntity = ctx.player();
            BlockPos entityPos = msg.blockPos;
            if (optionalPlayerEntity.isPresent()) {
                PlayerEntity player = optionalPlayerEntity.get();
                World world = player.getEntityWorld();
                if (world.isChunkLoaded(entityPos)) {
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(entityPos);
                    trashcanBlockEntity.clearContent();
                } else {
                    player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}