package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.SoundRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

public class SoundRegistryNeoForge {
    @SubscribeEvent
    public static void registerSounds(RegisterEvent event) {
        event.register(BuiltInRegistries.SOUND_EVENT.key(), soundEventRegisterHelper -> {
            SoundRegistry.registerSounds();
            SoundRegistryImpl.soundEventMap.forEach(soundEventRegisterHelper::register);
        });
    }
}
