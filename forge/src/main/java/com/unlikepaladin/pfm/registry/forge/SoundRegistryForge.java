package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistryForge {
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        SoundRegistry.registerSounds();
        event.getRegistry().registerAll(
                SoundRegistryImpl.soundEventList.toArray(new SoundEvent[0])
         );
    }
}
