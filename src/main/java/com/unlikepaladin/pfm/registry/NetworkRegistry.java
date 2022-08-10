package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
        ServerPlayNetworking.registerGlobalReceiver(MICROWAVE_ACTIVATE_PACKET_ID, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            server.submitAndJoin(() -> {
                if(Objects.nonNull(player.world.getBlockEntity(pos))){
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(active);
                }

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkRegistry.TOILET_USE_ID,
                ((server, player, handler, attachedData, responseSender) -> {
                    // Get the BlockPos we put earlier, in the networking thread
                    BlockPos blockPos = attachedData.readBlockPos();
                    server.submitAndJoin(() -> {
                        // Use the pos in the main thread
                        World world = player.world;
                        world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToilet.TOILET_STATE, ToiletState.DIRTY));
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
