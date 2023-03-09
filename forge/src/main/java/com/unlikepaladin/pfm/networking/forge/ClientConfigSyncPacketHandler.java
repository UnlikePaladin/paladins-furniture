package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.screens.MicrowaveScreen;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientConfigSyncPacketHandler {
    public static void handlePacket(SyncConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        System.out.println("Receiving Packet on Client: size of " + msg.configOptions.size());

        msg.configOptions.forEach((title, configOption) -> {
            if (configOption.getSide() == Side.SERVER) {
                PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
            }
        });

    }
}
