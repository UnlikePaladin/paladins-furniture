package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientSyncConfigPacketHandler {
    public static void handlePacket(SyncConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        msg.configOptions.forEach((title, configOption) -> {
            if (configOption.getSide() == Side.SERVER) {
                LeaveEventHandlerForge.originalConfigValues.put(title, PaladinFurnitureMod.getPFMConfig().options.get(title).getValue());
                PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
            }
        });
    }
}
