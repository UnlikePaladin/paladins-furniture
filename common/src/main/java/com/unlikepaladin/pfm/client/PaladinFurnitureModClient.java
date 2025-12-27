package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

public class PaladinFurnitureModClient {
    public static KeyBinding USE_TOILET_KEYBIND;

    public static final KeyBinding.Category PFM_CATEGORY = KeyBinding.Category.create(Identifier.of(PaladinFurnitureMod.MOD_ID, "main"));


    @ExpectPlatform
    public static boolean areShadersOn() {
        throw new AssertionError();
    }
}
