package com.unlikepaladin.pfm.ducks;

import com.mojang.blaze3d.platform.NativeImage;

public interface PFMSpriteExtensions {
    int pfm$getMipmapLevel();

    void pfm$setImages(NativeImage[] images);
}
