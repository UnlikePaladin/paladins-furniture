package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.Info.class)
public interface PFMSpriteInfoAccesor {
    @Accessor("animationData")
    AnimationResourceMetadata pfm$getAnimation();
}
