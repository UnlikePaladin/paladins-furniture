package com.unlikepaladin.pfm.mixin;


import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpriteContents.class)
public interface PFMSpriteContentsAccessor {
    @Accessor("byMipLevel")
    NativeImage[] pfm$getImages();

    @Accessor("byMipLevel")
    void pfm$setImages(NativeImage[] images);
}
