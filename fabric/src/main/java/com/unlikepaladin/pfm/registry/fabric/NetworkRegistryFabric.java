package com.unlikepaladin.pfm.registry.fabric;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.BasicToiletBlock;
import com.unlikepaladin.pfm.blocks.ToiletState;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.config.option.AbstractConfigOption;
import com.unlikepaladin.pfm.config.option.Side;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import com.unlikepaladin.pfm.registry.SoundIDs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class NetworkRegistryFabric {
    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_ACTIVATE_PACKET_ID, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            boolean active = attachedData.readBoolean();
            server.submitAndJoin(() -> {
                if(Objects.nonNull(player.world.getBlockEntity(pos))){
                    World world = player.world;
                    if (world.isChunkLoaded(pos)) {
                        MicrowaveBlockEntity microwaveBlockEntity = (MicrowaveBlockEntity) player.world.getBlockEntity(pos);
                        microwaveBlockEntity.setActive(active);
                    } else {
                        player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkIDs.TRASHCAN_CLEAR, (server, player, handler, attachedData, responseSender) -> {
            BlockPos pos = attachedData.readBlockPos();
            server.submitAndJoin(() -> {
                if(Objects.nonNull(player.world.getBlockEntity(pos))){
                    World world = player.world;
                    if (world.isChunkLoaded(pos)) {
                        TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) player.world.getBlockEntity(pos);
                        trashcanBlockEntity.clear();
                    } else {
                        player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                    }
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
                        if (world.isChunkLoaded(blockPos)) {
                            world.setBlockState(blockPos, world.getBlockState(blockPos).with(BasicToiletBlock.TOILET_STATE, ToiletState.DIRTY));
                            world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundIDs.TOILET_USED_EVENT, SoundCategory.BLOCKS, 0.3f, world.random.nextFloat() * 0.1f + 0.9f);
                        } else {
                            player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                        }
                    });
                }));
    }

    public static void registerClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.MICROWAVE_UPDATE_PACKET_ID,
            (client, handler, buf, responseSender) -> {
                boolean active = buf.readBoolean();
                BlockPos blockPos = buf.readBlockPos();
                if (handler.getWorld().isChunkLoaded(blockPos)) {
                    MicrowaveBlockEntity blockEntity = (MicrowaveBlockEntity) handler.getWorld().getBlockEntity(blockPos);
                    client.execute(() -> {
                        if (Objects.nonNull(client.currentScreen) && client.currentScreen instanceof MicrowaveScreen currentScreen)  {
                            currentScreen.getScreenHandler().setActive(blockEntity, active);}
                    });
                }
                else {
                    client.player.sendMessage(Text.of("Trying to access unloaded chunks, are you cheating?"), false);
                }
            }
        );

        ClientPlayNetworking.registerGlobalReceiver(NetworkIDs.CONFIG_SYNC_ID,
                (client, handler, buf, responseSender) -> {
                    ArrayList<AbstractConfigOption> configOptions = buf.readCollection(Lists::newArrayListWithCapacity, AbstractConfigOption::readConfigOption);
                    Map<String, AbstractConfigOption> map = new HashMap<>();
                    configOptions.forEach(abstractConfigOption -> {
                        map.put(((TranslatableText)abstractConfigOption.getTitle()).getKey(), abstractConfigOption);
                    });

                    client.execute(() -> {
                        map.forEach((title, configOption) -> {
                            if (configOption.getSide() == Side.SERVER) {
                                PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
                                try {
                                    PaladinFurnitureMod.getPFMConfig().save();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    });
                }
            );
        }
}
