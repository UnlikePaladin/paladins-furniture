package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class NetworkRegistry {
    public static Identifier MICROWAVE_UPDATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_button_update");
    public static final Identifier MICROWAVE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_activate");

    public static void registerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(MICROWAVE_PACKET_ID, (packetContext, attachedData) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            packetContext.getTaskQueue().execute(() -> {
                if(Objects.nonNull(packetContext.getPlayer().world.getBlockEntity(pos))){
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(active);
                }

            });
        });
    }

    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(NetworkRegistry.MICROWAVE_UPDATE_PACKET_ID,
                (packetContext, attachedData) -> {
                    // Get the BlockPos we put earlier, in the networking thread
                    boolean active = attachedData.readBoolean();
                    packetContext.getTaskQueue().execute(() -> {
                        // Use the pos in the main thread
                        if (Objects.nonNull(MinecraftClient.getInstance().currentScreen))  {
                            MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                            currentScreen.getScreenHandler().setActive(active);}
                    });
                });
    }
}
