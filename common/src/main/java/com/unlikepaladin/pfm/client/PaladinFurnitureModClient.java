package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

public class PaladinFurnitureModClient {
    public static KeyMapping USE_TOILET_KEYBIND;

    public static final KeyMapping.Category PFM_CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "main"));


    @ExpectPlatform
    public static boolean areShadersOn() {
        throw new AssertionError();
    }
}
