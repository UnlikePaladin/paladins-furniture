package com.unlikepaladin.pfm.registry.neoforge;

import com.unlikepaladin.pfm.registry.SoundRegistry;
import net.minecraft.registry.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "pfm", bus = EventBusSubscriber.Bus.MOD)
public class SoundRegistryNeoForge {
    @SubscribeEvent
    public static void registerSounds(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT.getKey(), soundEventRegisterHelper -> {
            SoundRegistry.registerSounds();
            SoundRegistryImpl.soundEventMap.forEach(soundEventRegisterHelper::register);
        });
    }
}
