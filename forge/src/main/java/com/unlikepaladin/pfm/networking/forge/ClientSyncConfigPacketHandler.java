package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import com.unlikepaladin.pfm.config.option.Side;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class ClientSyncConfigPacketHandler {
    public static void handlePacket(SyncConfigPacket msg, CustomPayloadEvent.Context ctx) {
        PFMConfigScreen.isOnServer = true;
        msg.configOptions.forEach((title, configOption) -> {
            if (configOption.getSide() == Side.SERVER) {
                LeaveEventHandlerForge.originalConfigValues.put(title, PaladinFurnitureMod.getPFMConfig().options.get(title).getValue());
                PaladinFurnitureMod.getPFMConfig().options.get(title).setValue(configOption.getValue());
            }
        });
    }
}
