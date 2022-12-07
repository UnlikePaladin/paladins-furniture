package com.unlikepaladin.pfm.registry.fabric;

import com.unlikepaladin.pfm.registry.SoundIDs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SoundRegistryFabric {
    public static void registerSounds(){
        Registry.register(Registries.SOUND_EVENT, SoundIDs.MICROWAVE_BEEP_ID, SoundIDs.MICROWAVE_BEEP_EVENT);
        Registry.register(Registries.SOUND_EVENT, SoundIDs.MICROWAVE_RUNNING_ID, SoundIDs.MICROWAVE_RUNNING_EVENT);
        Registry.register(Registries.SOUND_EVENT, SoundIDs.TOILET_FLUSHING_ID, SoundIDs.TOILET_FLUSHING_EVENT);
        Registry.register(Registries.SOUND_EVENT, SoundIDs.TOILET_USED_ID, SoundIDs.TOILET_USED_EVENT);
    }
}
