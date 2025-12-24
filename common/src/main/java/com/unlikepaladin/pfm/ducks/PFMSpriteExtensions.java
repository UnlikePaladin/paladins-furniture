package com.unlikepaladin.pfm.ducks;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.util.Identifier;

public interface PFMSpriteExtensions {
    int pfm$getMipmapLevel();

    void pfm$setImages(NativeImage[] images);

    void pfm$setContents(SpriteContents contents);

    Identifier pfm$getId();
}
