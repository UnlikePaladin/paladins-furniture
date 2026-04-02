package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MicrowaveUpdatePacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "microwave_update");
    public final BlockPos entityPos;
    public final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    public MicrowaveUpdatePacket(FriendlyByteBuf buffer) {
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
    public void write(FriendlyByteBuf buffer) {
        BlockPos entityPos = this.entityPos;
        boolean active = this.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
