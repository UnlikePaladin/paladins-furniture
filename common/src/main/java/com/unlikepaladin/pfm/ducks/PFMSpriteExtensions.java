package com.unlikepaladin.pfm.ducks;

import net.minecraft.client.texture.NativeImage;

public interface PFMSpriteExtensions {
    int pfm$getMipmapLevel();

    void pfm$setImages(NativeImage[] images);
}
