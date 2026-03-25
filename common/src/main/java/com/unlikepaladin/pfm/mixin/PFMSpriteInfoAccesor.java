package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureAtlasSprite.Info.class)
public interface PFMSpriteInfoAccesor {
    @Accessor("metadata")
    AnimationMetadataSection pfm$getAnimation();
}
