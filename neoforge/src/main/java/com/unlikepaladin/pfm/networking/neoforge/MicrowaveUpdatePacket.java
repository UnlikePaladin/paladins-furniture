package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MicrowaveUpdatePacket implements CustomPayload {
    public static final Identifier ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_update");
    public final BlockPos entityPos;
    public final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    public MicrowaveUpdatePacket(PacketByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    // In Packet class
    public static void handle(MicrowaveUpdatePacket msg, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() ->
                // Make sure it's only executed on the physical client
                {
                    if (FMLEnvironment.dist.isClient()) {
                        ClientMicrowaveUpdatePackeHandler.handlePacket(msg, ctx);
                    }
                }
        );
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
