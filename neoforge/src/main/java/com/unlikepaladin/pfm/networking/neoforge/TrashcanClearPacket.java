package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

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
                    trashcanBlockEntity.clear();
                } else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}