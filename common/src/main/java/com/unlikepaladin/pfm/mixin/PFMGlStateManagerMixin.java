package com.unlikepaladin.pfm.mixin;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlStateManager.class)
public interface PFMGlStateManagerMixin {
    @Accessor("activeTexture")
    static int pfm$getActiveTexture() {
        throw new UnsupportedOperationException();
    }
}
