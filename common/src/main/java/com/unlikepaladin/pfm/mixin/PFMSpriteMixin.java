package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.*;

@Mixin(Sprite.class)
public abstract class PFMSpriteMixin implements PFMSpriteExtensions {
    @Mutable
    @Shadow
    @Final
    private SpriteContents contents;

    @Override
    public int pfm$getMipmapLevel() {
        return (((PFMSpriteContentsAccessor)contents).pfm$getImages().length-1);
    }

    @Override
    public void pfm$setImages(NativeImage[] images) {
        ((PFMSpriteContentsAccessor)this.contents).pfm$setImages(images);
    }

    @Override
    public void pfm$setContents(SpriteContents contents) {
        this.contents = contents;
    }
}
