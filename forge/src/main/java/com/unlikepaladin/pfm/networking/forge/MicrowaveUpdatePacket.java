package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MicrowaveUpdatePacket {

    /*         boolean active = buf.readBoolean();
                    MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) handler.getWorld().getBlockEntity(buf.readBlockPos());
                    client.execute(() -> {

                    });*/

    private final BlockPos entityPos;
    private final boolean active;

    public MicrowaveUpdatePacket(BlockPos entityPos, boolean active) {
        this.entityPos = entityPos;
        this.active = active;
    }

    // In Packet class
    public static void handle(MicrowaveUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MicrowaveUpdatePacket.handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

    // In ClientPacketHandlerClass
    public static void handlePacket(MicrowaveUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        BlockPos blockPos = msg.entityPos;
        boolean active = msg.active;
        World world = ctx.get().getSender().world;
        if (world.isChunkLoaded(blockPos)) {
            MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) world.getBlockEntity(blockPos);
                if (Objects.nonNull(MinecraftClient.getInstance().currentScreen) && MinecraftClient.getInstance().currentScreen instanceof MicrowaveScreen)  {
                    MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                    currentScreen.getScreenHandler().setActive(blockEntity, active);
                }
                else {
                    Objects.requireNonNull(ctx.get().getSender()).sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
        }
    }

    public static void encode(MicrowaveUpdatePacket packet, PacketByteBuf buffer) {
        BlockPos entityPos = packet.entityPos;
        boolean active = packet.active;
        buffer.writeBlockPos(entityPos);
        buffer.writeBoolean(active);
    }

    public static MicrowaveUpdatePacket decode(PacketByteBuf buffer) {
        BlockPos entityPos = buffer.readBlockPos();
        boolean active = buffer.readBoolean();
        return new MicrowaveUpdatePacket(entityPos, active);
    }

}
