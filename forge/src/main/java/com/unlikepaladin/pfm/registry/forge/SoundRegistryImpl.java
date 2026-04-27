package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SoundRegistryImpl {
    public static List<SoundEvent> soundEventList = new ArrayList<>();
    public static void register(ResourceLocation identifier, SoundEvent event) {
        event.setRegistryName(identifier);
        soundEventList.add(event);
    }
}
