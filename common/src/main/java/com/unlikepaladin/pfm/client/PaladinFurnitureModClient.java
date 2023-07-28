package com.unlikepaladin.pfm.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.option.KeyBinding;

public class PaladinFurnitureModClient {
    public static KeyBinding USE_TOILET_KEYBIND;

    @ExpectPlatform
    public static boolean areShadersOn() {
        throw new AssertionError();
    }
}
