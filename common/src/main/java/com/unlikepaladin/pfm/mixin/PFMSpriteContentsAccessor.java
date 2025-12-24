package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpriteContents.class)
public interface PFMSpriteContentsAccessor {
    @Accessor("mipmapLevelsImages")
    NativeImage[] pfm$getImages();

    @Accessor("mipmapLevelsImages")
    void pfm$setImages(NativeImage[] images);
}
