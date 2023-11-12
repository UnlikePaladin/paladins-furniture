package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanguageManager.class)
public interface PFMLanguageManagerAccessor {
    @Accessor("currentLanguageCode")
    String getCurrentLanguageCode();

    @Accessor("ENGLISH_US")
    public static LanguageDefinition getEnglish_Us() {
        throw new AssertionError();
    }
}