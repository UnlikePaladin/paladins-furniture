package com.unlikepaladin.pfm.registry.forge;

import com.unlikepaladin.pfm.registry.SoundIDs;
import com.unlikepaladin.pfm.registry.SoundRegistry;
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
        SoundRegistry.registerSounds();
            soundEventRegisterHelper.registerAll(
                SoundRegistryImpl.soundEventList.toArray(new SoundEvent[0])
         );
    });
}
