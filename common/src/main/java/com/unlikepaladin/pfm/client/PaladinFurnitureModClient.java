package com.unlikepaladin.pfm.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;

public class PaladinFurnitureModClient {
    public static KeyMapping USE_TOILET_KEYBIND;

    @ExpectPlatform
    public static boolean areShadersOn() {
        throw new AssertionError();
    }
}
