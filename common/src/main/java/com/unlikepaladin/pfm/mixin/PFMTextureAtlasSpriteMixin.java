package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;

@Mixin(TextureAtlasSprite.class)
public abstract class PFMTextureAtlasSpriteMixin implements PFMSpriteExtensions {
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

    @Override
    public ResourceLocation pfm$getId() {
        return contents.getId();
    }
}
