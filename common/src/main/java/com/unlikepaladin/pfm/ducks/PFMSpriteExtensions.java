package com.unlikepaladin.pfm.ducks;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.ResourceLocation;

public interface PFMSpriteExtensions {
    int pfm$getMipmapLevel();

    void pfm$setImages(NativeImage[] images);

    void pfm$setContents(SpriteContents contents);

    ResourceLocation pfm$getId();
}
