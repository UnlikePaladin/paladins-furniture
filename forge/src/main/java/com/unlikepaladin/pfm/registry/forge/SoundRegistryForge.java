package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;

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
