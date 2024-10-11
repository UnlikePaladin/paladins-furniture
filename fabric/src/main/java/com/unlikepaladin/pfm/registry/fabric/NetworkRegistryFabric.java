package com.unlikepaladin.pfm.registry.fabric;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.networking.*;
import com.unlikepaladin.pfm.networking.fabric.LeaveEventHandlerFabric;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class NetworkRegistryFabric {
    public static void registerPackets() {
        PayloadTypeRegistry.playC2S().register(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, MicrowaveActivatePayload.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID,
                (payload, context) -> {payload.handle(context.server(), context.player());});

        PayloadTypeRegistry.playC2S().register(NetworkIDs.TRASHCAN_CLEAR, TrashcanClearPayload.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TRASHCAN_CLEAR,
                (payload, context) -> {payload.handle(context.server(), context.player());});

        PayloadTypeRegistry.playC2S().register(NetworkIDs.TOILET_USE_ID, ToiletUsePayload.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TOILET_USE_ID,
                ((payload, context) -> payload.handle(context.server(), context.player())));
    }

    public static void registerClientPackets() {
        PayloadTypeRegistry.playS2C().register(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID, MicrowaveUpdatePayload.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID, (payload, context) -> { payload.handle(context.player(), context.client());});
        PayloadTypeRegistry.playS2C().register(NetworkIDs.CONFIG_SYNC_ID, SyncConfigPayload.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.CONFIG_SYNC_ID,
                (payload, context) -> {
                    context.client().execute(() -> {
                        payload.configOptionMap().forEach((title, configOption) -> {
                            PFMConfigScreen.isOnServer = true;
                            if (configOption.getSide() == Side.SERVER) {
                                LeaveEventHandlerFabric.originalConfigValues.put(title, PaladinFurnitureMod.getPFMConfig().options.get(title).getValue());
                                PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
                            }
                        });
                    });
                }
            );
        }
}
