package com.unlikepaladin.pfm.networking.forge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.screens.PFMConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LeaveEventHandlerForge {
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
