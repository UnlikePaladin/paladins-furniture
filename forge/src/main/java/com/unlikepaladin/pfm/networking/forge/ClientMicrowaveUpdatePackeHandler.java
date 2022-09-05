package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientMicrowaveUpdatePackeHandler {

    // In ClientPacketHandlerClass
    public static void handlePacket(MicrowaveUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        BlockPos blockPos = msg.entityPos;
        boolean active = msg.active;
        World world = MinecraftClient.getInstance().world;
        if (world.isChunkLoaded(blockPos)) {
            MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) world.getBlockEntity(blockPos);
            if (Objects.nonNull(MinecraftClient.getInstance().currentScreen) && MinecraftClient.getInstance().currentScreen instanceof MicrowaveScreen)  {
                MicrowaveScreen microwaveScreen = (MicrowaveScreen)MinecraftClient.getInstance().currentScreen;
                microwaveScreen.getScreenHandler().setActive(blockEntity, active);
            }
        }
        else {
            Objects.requireNonNull(ctx.get().getSender()).sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}
