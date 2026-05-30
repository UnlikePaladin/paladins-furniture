package com.unlikepaladin.pfm.registry.fabric;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;

public class SoundRegistryImpl {
    public static void register(Identifier identifier, SoundEvent event) {
        Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, event);
    }
}
