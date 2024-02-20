package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

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
                PlayerEntity player = ctx.player().get();
                World world = player.getEntityWorld();
                if (world.isChunkLoaded(entityPos)) {
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) world.getBlockEntity(entityPos);
                    microwaveBlockEntity.setActive(active);
                }
                else {
                    player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        });
    }

    @Override
    public void write(PacketByteBuf buffer) {
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

