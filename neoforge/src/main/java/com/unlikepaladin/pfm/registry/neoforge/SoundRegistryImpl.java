package com.unlikepaladin.pfm.registry.neoforge;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class SoundRegistryImpl {
    public static Map<ResourceLocation, SoundEvent> soundEventMap = new LinkedHashMap<>();
    public static void register(ResourceLocation identifier, SoundEvent event) {
        soundEventMap.put(identifier, event);
    }
}
