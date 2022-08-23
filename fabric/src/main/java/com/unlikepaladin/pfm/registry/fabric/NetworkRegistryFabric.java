package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.Statistics;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class NetworkRegistryFabric {

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            server.submitAndJoin(() -> {
                if(Objects.nonNull(player.world.getBlockEntity(pos))){
                    MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.world.getBlockEntity(pos);
                    microwaveBlockEntity.setActive(active);
                }

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TOILET_USE_ID,
                ((server, player, handler, attachedData, responseSender) -> {
                    // Get the BlockPos we put earlier, in the networking thread
                    BlockPos blockPos = attachedData.readBlockPos();
                    server.submitAndJoin(() -> {
                        // Use the pos in the main thread
                        World world = player.world;
                        world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToilet.TOILET_STATE, ToiletState.DIRTY));
                        world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                    });
                }));
    }

    public static void registerClientPackets() {
            ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    boolean active = buf.readBoolean();
                    MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) handler.getWorld().getBlockEntity(buf.readBlockPos());
                    client.execute(() -> {
                        if (Objects.nonNull(MinecraftClient.getInstance().currentScreen) && MinecraftClient.getInstance().currentScreen instanceof MicrowaveScreen)  {
                            MicrowaveScreen currentScreen = (MicrowaveScreen) MinecraftClient.getInstance().currentScreen;
                            currentScreen.getScreenHandler().setActive(blockEntity, active);}
                    });
                });
            }

}
