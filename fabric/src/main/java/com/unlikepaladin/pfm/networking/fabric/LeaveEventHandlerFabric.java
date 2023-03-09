package com.unlikepaladin.pfm.networking.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.registry.fabric.NetworkRegistryFabric;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.io.IOException;
import java.util.HashMap;

public class LeaveEventHandlerFabric {
    public static final HashMap<String, Object> originalConfigValues = new HashMap<>();
    public static void onServerLeave(ClientPlayNetworkHandler handler, MinecraftClient client) {
        originalConfigValues.forEach((key, value) -> PaladinFurnitureMod.getPFMConfig().options.get(key).setValue(value));
        try {
            PaladinFurnitureMod.getPFMConfig().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
