package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToilet;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
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
                    PlayerEntity playerEntity = packetContext.getPlayer();
                    BlockPos blockPos = attachedData.readBlockPos();
                    packetContext.getTaskQueue().execute(() -> {
                        playerEntity.incrementStat(StatisticsRegistry.TOILET_USED);
                        World world = packetContext.getPlayer().world;
                        world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToilet.TOILET_STATE, ToiletState.DIRTY));
                        world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundRegistry.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
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
