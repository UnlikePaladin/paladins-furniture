package com.unlikepaladin.pfm.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NativeImage.class)
public interface PFMNativeImageAccessor {

    @Invoker("getPixelABGR")
    int pfm$getColor(int x, int y);
}
