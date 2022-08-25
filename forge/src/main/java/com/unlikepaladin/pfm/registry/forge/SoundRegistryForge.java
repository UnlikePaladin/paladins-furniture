package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistryForge {
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
         event.getRegistry().registerAll(
                registerSound(SoundIDs.MICROWAVE_BEEP_EVENT, SoundIDs.MICROWAVE_BEEP_ID.getPath()),
                registerSound(SoundIDs.MICROWAVE_RUNNING_EVENT, SoundIDs.MICROWAVE_RUNNING_ID.getPath()),
                registerSound(SoundIDs.TOILET_FLUSHING_EVENT, SoundIDs.TOILET_FLUSHING_ID.getPath()),
                registerSound(SoundIDs.TOILET_USED_EVENT, SoundIDs.TOILET_USED_ID.getPath())
         );
    }

    private static SoundEvent registerSound(SoundEvent soundEvent, String name) {
        return soundEvent.setRegistryName(name);
    }
}
