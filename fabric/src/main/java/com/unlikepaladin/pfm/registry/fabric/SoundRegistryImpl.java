package com.unlikepaladin.pfm.registry.fabric;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SoundRegistryImpl {
    public static void register(Identifier identifier, SoundEvent event) {
        Registry.register(Registry.SOUND_EVENT, identifier, event);
    }
}
