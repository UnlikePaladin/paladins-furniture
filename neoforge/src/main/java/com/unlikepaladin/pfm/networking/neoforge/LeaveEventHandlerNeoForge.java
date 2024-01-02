package com.unlikepaladin.pfm.networking.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;


import java.io.IOException;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LeaveEventHandlerNeoForge {
    public static final HashMap<String, Object> originalConfigValues = new HashMap<>();

    @SubscribeEvent
    public static void onServerLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        PFMConfigScreen.isOnServer = false;
        originalConfigValues.forEach((key, value) -> {
            PaladinFurnitureMod.getPFMConfig().options.get(key).setValue(value);
        });
        try {
            PaladinFurnitureMod.getPFMConfig().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
