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
import com.unlikepaladin.pfm.networking.fabric.LeaveEventHandlerFabric;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.*;

public class NetworkRegistryFabric {
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            server.executeBlocking(() -> {
                if(Objects.nonNull(player.level.getBlockEntity(pos))){
                    Level level = player.level;
                    if (level.hasChunkAt(pos)) {
                        MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.level.getBlockEntity(pos);
                        microwaveBlockEntity.setActive(active);
                    } else {
                        player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TRASHCAN_CLEAR, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            server.executeBlocking(() -> {
                if(Objects.nonNull(player.level.getBlockEntity(pos))){
                    Level level = player.level;
                    if (level.hasChunkAt(pos)) {
                        TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) player.level.getBlockEntity(pos);
                        trashcanBlockEntity.clearContent();
                    } else {
                        player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TOILET_USE_ID,
                ((server, player, handler, attachedData, responseSender) -> {
                    // Get the BlockPos we put earlier, in the networking thread
                    BlockPos blockPos = attachedData.readBlockPos();
                    server.executeBlocking(() -> {
                        // Use the pos in the main thread
                        Level level = player.level;
                        if (level.hasChunkAt(blockPos)) {
                            level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundSource.BLOCKS, 0.3f, level.random.nextFloat() * 0.1f + 0.9f);
                        } else {
                            player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                        }
                    });
                }));
    }

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID,
            (client, handler, buf, responseSender) -> {
                boolean active = buf.readBoolean();
                BlockPos blockPos = buf.readBlockPos();
                if (handler.getLevel().hasChunkAt(blockPos)) {
                    MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) handler.getLevel().getBlockEntity(blockPos);
                    client.execute(() -> {
                        if (Objects.nonNull(client.screen) && client.screen instanceof MicrowaveScreen currentScreen)  {
                            currentScreen.getMenu().setActive(blockEntity, active);}
                    });
                }
                else {
                    client.player.displayClientMessage(Component.nullToEmpty("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        );
        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.CONFIG_SYNC_ID,
                (client, handler, buf, responseSender) -> {
                    ArrayList<AbstractConfigOption> configOptions = buf.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
                    Map<String, AbstractConfigOption> map = new HashMap<>();
                    configOptions.forEach(abstractConfigOption -> {
                        map.put(((TranslatableContents)abstractConfigOption.getTitle().getContents()).getKey(), abstractConfigOption);
                    });

                    client.execute(() -> {
                        map.forEach((title, configOption) -> {
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
