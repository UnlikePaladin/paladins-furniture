package com.unlikepaladin.pfm.registry;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundIDs {
    public static final Identifier MICROWAVE_BEEP_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "microwave_beep");
    public static SoundEvent MICROWAVE_BEEP_EVENT = SoundEvent.of(MICROWAVE_BEEP_ID);
    public static final Identifier MICROWAVE_RUNNING_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "microwave_running");
    public static SoundEvent MICROWAVE_RUNNING_EVENT = SoundEvent.of(MICROWAVE_RUNNING_ID);

    public static final Identifier TOILET_FLUSHING_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "toilet_flushing");
    public static SoundEvent TOILET_FLUSHING_EVENT = SoundEvent.of(TOILET_FLUSHING_ID);
    public static final Identifier TOILET_USED_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "toilet_used");
    public static SoundEvent TOILET_USED_EVENT = SoundEvent.of(TOILET_USED_ID);
}


