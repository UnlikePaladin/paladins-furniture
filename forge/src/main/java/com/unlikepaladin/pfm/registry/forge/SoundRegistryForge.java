package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.SoundRegistry;
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
        SoundRegistry.registerSounds();
        event.getRegistry().registerAll(
                SoundRegistryImpl.soundEventList.toArray(new SoundEvent[0])
         );
    }
}
