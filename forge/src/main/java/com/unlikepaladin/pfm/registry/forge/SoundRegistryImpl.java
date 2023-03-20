package com.unlikepaladin.pfm.registry.forge;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SoundRegistryImpl {
    public static List<SoundEvent> soundEventList = new ArrayList<>();
    public static void register(Identifier identifier, SoundEvent event) {
        event.setRegistryName(identifier);
        soundEventList.add(event);
    }
}
