package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class TrashcanClearPacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "trashcan_clear");
    private final BlockPos blockPos;

    public TrashcanClearPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public TrashcanClearPacket(BlockPos pos) {
        this.blockPos = pos;
    }

    public static void handle(TrashcanClearPacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            Optional<Player> optionalPlayerEntity = ctx.player();
            BlockPos entityPos = msg.blockPos;
            if (optionalPlayerEntity.isPresent()) {
                Player player = optionalPlayerEntity.get();
                Level world = player.level();
                if (world.hasChunkAt(entityPos)) {
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
    public ResourceLocation id() {
        return ID;
    }
}