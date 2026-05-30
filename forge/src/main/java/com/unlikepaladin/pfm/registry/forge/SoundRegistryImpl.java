package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class SoundRegistryImpl {
    public static Map<Identifier, SoundEvent> soundEventMap = new LinkedHashMap<>();
    public static void register(Identifier identifier, SoundEvent event) {
        soundEventMap.put(identifier, event);
    }
}
