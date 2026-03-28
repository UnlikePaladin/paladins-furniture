package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistryForge {
    @SubscribeEvent
    public static void registerSounds(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, soundEventRegisterHelper -> {
            SoundRegistry.registerSounds();
            SoundRegistryImpl.soundEventMap.forEach(soundEventRegisterHelper::register);
        });
    }
}
