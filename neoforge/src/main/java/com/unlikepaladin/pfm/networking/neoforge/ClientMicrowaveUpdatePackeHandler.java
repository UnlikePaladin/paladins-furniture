package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientMicrowaveUpdatePackeHandler {
    public static void handlePacket(MicrowaveUpdatePacket msg, NetworkEvent.Context ctx) {
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
            Objects.requireNonNull(ctx.getSender()).displayClientMessage(Component.literal("Trying to access unloaded chunks, are you cheating?"), false);
        }
    }
}