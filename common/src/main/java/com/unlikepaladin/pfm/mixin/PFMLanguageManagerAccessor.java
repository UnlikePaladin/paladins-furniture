package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LanguageManager.class)
public interface PFMLanguageManagerAccessor {
    @Accessor
    String getCurrentLanguageCode();

    @Accessor("field_25291")
    static LanguageDefinition getEnglish_Us() {
        throw new AssertionError();
    }
}