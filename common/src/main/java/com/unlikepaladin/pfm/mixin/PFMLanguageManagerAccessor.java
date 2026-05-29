package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanguageManager.class)
public interface PFMLanguageManagerAccessor {
    @Accessor("currentCode")
    String getCurrentCode();

    @Accessor("DEFAULT_LANGUAGE")
    public static LanguageInfo getEnglish_Us() {
        throw new AssertionError();
    }
}