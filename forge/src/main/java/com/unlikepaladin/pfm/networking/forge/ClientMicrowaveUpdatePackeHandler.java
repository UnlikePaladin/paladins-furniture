package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Objects;

public class ClientMicrowaveUpdatePackeHandler {
    public static void handlePacket(MicrowaveUpdatePacket msg, CustomPayloadEvent.Context ctx) {
        BlockPos blockPos = msg.entityPos;
        boolean active = msg.active;
        World world = MinecraftClient.getInstance().world;
        if (world.isChunkLoaded(blockPos)) {
            MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) world.getBlockEntity(blockPos);
            if (Objects.nonNull(MinecraftClient.getInstance().currentScreen) && MinecraftClient.getInstance().currentScreen instanceof MicrowaveScreen microwaveScreen)  {
                microwaveScreen.getScreenHandler().setActive(blockEntity, active);
            }
        }
        else {
            Objects.requireNonNull(ctx.getSender()).sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}
