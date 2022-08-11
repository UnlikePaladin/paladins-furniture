package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SoundRegistry {
    public static final Identifier MICROWAVE_BEEP_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_beep");
    public static SoundEvent MICROWAVE_BEEP_EVENT = new SoundEvent(MICROWAVE_BEEP_ID);
    public static final Identifier MICROWAVE_RUNNING_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "microwave_running");
    public static SoundEvent MICROWAVE_RUNNING_EVENT = new SoundEvent(MICROWAVE_RUNNING_ID);

    public static final Identifier TOILET_FLUSHING_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_flushing");
    public static SoundEvent TOILET_FLUSHING_EVENT = new SoundEvent(TOILET_FLUSHING_ID);
    public static final Identifier TOILET_USED_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "toilet_used");
    public static SoundEvent TOILET_USED_EVENT = new SoundEvent(TOILET_USED_ID);
    public static void registerSounds(){
        Registry.register(Registry.SOUND_EVENT, MICROWAVE_BEEP_ID, MICROWAVE_BEEP_EVENT);
        Registry.register(Registry.SOUND_EVENT, MICROWAVE_RUNNING_ID, MICROWAVE_RUNNING_EVENT);
        Registry.register(Registry.SOUND_EVENT, TOILET_FLUSHING_ID, TOILET_FLUSHING_EVENT);
        Registry.register(Registry.SOUND_EVENT, TOILET_USED_ID, TOILET_USED_EVENT);
    }
}


