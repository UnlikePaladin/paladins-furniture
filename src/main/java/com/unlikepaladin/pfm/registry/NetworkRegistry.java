package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class NetworkRegistry {
    public static Identifier MICROWAVE_UPDATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_button_update");
    public static final Identifier MICROWAVE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_activate");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(MICROWAVE_PACKET_ID, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            server.submitAndJoin(() -> {
                if(Objects.nonNull(player.world.getBlockEntity(pos))){
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(active);
                }

            });
        });
    }

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(MICROWAVE_UPDATE_PACKET_ID, (client, handler, buf, responseSender) -> {
            boolean active = buf.readBoolean();
            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                if (Objects.nonNull(MinecraftClient.getInstance().currentScreen)) {
                    MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                    currentScreen.getScreenHandler().setActive(active);
                }
            });
        });
    }
}
