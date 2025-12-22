package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Sprite.class)
public abstract class PFMSpriteMixin implements PFMSpriteExtensions {
    @Mutable
    @Shadow
    @Final
    public NativeImage[] images;

    @Override
    public int pfm$getMipmapLevel() {
        return (images.length-1);
    }

    @Override
    public void pfm$setImages(NativeImage[] images) {
        this.images = images;
    }
}
