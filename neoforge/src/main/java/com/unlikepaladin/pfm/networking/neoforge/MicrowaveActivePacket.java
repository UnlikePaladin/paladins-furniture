package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

public class MicrowaveActivePacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_active");
    private final BlockPos entityPos;
    private final boolean active;

    public MicrowaveActivePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    public MicrowaveActivePacket(PacketByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    public static void handle(MicrowaveActivePacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            // Work that needs to be thread-safe (most work)
            // Do stuffm
            BlockPos entityPos = msg.entityPos;
            boolean active = msg.active;
            if (ctx.player().isPresent()) {
                Player player = ctx.player().get();
                Level world = player.level();
                if (world.isChunkLoaded(entityPos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(entityPos);
                    microwaveBlockEntity.setActive(active);
                }
                else {
                    player.displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        BlockPos entityPos = this.entityPos;
        boolean active = this.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}

