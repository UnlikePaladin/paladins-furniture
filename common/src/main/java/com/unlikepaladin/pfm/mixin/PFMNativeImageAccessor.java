package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NativeImage.class)
public interface PFMNativeImageAccessor {

    @Invoker("getColor")
    int pfm$getColor(int x, int y);
}
