package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistryForge {
    @SubscribeEvent
    public static void registerSounds(RegisterEvent event) {
         event.register(ForgeRegistries.Keys.SOUND_EVENTS, soundEventRegisterHelper -> {
             soundEventRegisterHelper.register(SoundIDs.MICROWAVE_BEEP_ID, SoundIDs.MICROWAVE_BEEP_EVENT);
             soundEventRegisterHelper.register(SoundIDs.MICROWAVE_RUNNING_ID, SoundIDs.MICROWAVE_RUNNING_EVENT);
             soundEventRegisterHelper.register(SoundIDs.TOILET_FLUSHING_ID, SoundIDs.TOILET_FLUSHING_EVENT);
             soundEventRegisterHelper.register(SoundIDs.TOILET_USED_ID, SoundIDs.TOILET_USED_EVENT);

         });
    }

    private static SoundEvent registerSound(SoundEvent soundEvent, String name) {
        return soundEvent;
    }
}
