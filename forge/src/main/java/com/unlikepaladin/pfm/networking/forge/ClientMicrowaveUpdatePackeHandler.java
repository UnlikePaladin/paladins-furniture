package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Objects;

public class ClientMicrowaveUpdatePackeHandler {
    public static void handlePacket(MicrowaveUpdatePacket msg, CustomPayloadEvent.Context ctx) {
        BlockPos blockPos = msg.entityPos;
        boolean active = msg.active;
        Level world = Minecraft.getInstance().level;
        if (world.hasChunkAt(blockPos)) {
            MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) world.getBlockEntity(blockPos);
            if (Objects.nonNull(Minecraft.getInstance().screen) && Minecraft.getInstance().screen instanceof MicrowaveScreen microwaveScreen)  {
                microwaveScreen.getMenu().setActive(blockEntity, active);
            }
        }
        else {
            Objects.requireNonNull(ctx.getSender()).displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}
