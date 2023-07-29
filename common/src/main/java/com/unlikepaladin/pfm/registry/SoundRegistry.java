package com.unlikepaladin.pfm.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static void registerSounds() {
        register(SoundIDs.MICROWAVE_BEEP_ID, SoundIDs.MICROWAVE_BEEP_EVENT);
        register(SoundIDs.MICROWAVE_RUNNING_ID, SoundIDs.MICROWAVE_RUNNING_EVENT);
        register(SoundIDs.TOILET_FLUSHING_ID, SoundIDs.TOILET_FLUSHING_EVENT);
        register(SoundIDs.TOILET_USED_ID, SoundIDs.TOILET_USED_EVENT);
    }
    @ExpectPlatform
    public static void register(Identifier identifier, SoundEvent event) {
        throw new RuntimeException();
    }
}
