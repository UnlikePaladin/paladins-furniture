package com.unlikepaladin.pfm.registry.neoforge;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class SoundRegistryImpl {
    public static Map<Identifier, SoundEvent> soundEventMap = new LinkedHashMap<>();
    public static void register(Identifier identifier, SoundEvent event) {
        soundEventMap.put(identifier, event);
    }
}
