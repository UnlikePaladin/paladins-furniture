package com.unlikepaladin.pfm.registry.fabric;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class SoundRegistryImpl {
    public static void register(Identifier identifier, SoundEvent event) {
        Registry.register(Registries.SOUND_EVENT, identifier, event);
    }
}
