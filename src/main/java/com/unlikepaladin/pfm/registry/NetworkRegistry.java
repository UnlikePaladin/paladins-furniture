package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class NetworkRegistry {
    public static Identifier MICROWAVE_UPDATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_button_update");
    public static final Identifier MICROWAVE_ACTIVATE_PACKET_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_activate");

    public static Identifier TOILET_USE_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_use");

    public static void registerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(NetworkRegistry.MICROWAVE_ACTIVATE_PACKET_ID, (packetContext, attachedData) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            packetContext.getTaskQueue().execute(() -> {
                if(Objects.nonNull(packetContext.getPlayer().world.getBlockEntity(pos))){
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(active);
                }

            });
        });

        ServerSidePacketRegistry.INSTANCE.register(NetworkRegistry.TOILET_USE_ID,
                (packetContext, attachedData) -> {
                    // Get the BlockPos we put earlier, in the networking thread
                    BlockPos blockPos = attachedData.readBlockPos();
                    packetContext.getTaskQueue().execute(() -> {
                        // Use the pos in the main thread
                        World world = packetContext.getPlayer().world;
                        world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToilet.TOILET_STATE, ToiletState.DIRTY));
                    });
                });
    }

    public static void registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(NetworkRegistry.MICROWAVE_UPDATE_PACKET_ID,
                (packetContext, attachedData) -> {
                    boolean active = attachedData.readBoolean();
                    packetContext.getTaskQueue().execute(() -> {
                        if (Objects.nonNull(MinecraftClient.getInstance().currentScreen) && MinecraftClient.getInstance().currentScreen instanceof MicrowaveScreen)  {
                            MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                            currentScreen.getScreenHandler().setActive(active);}
                    });
                });

    }
}
