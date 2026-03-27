package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextureAtlasSprite.class)
public abstract class PFMTextureAtlasSpriteMixin implements PFMSpriteExtensions {
    @Mutable
    @Shadow
    @Final
    public NativeImage[] mainImage;

    @Override
    public int pfm$getMipmapLevel() {
        return (mainImage.length-1);
    }

    @Override
    public void pfm$setImages(NativeImage[] images) {
        this.mainImage = images;
    }
}
