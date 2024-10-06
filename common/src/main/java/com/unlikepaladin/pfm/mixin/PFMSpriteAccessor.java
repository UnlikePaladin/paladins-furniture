package com.unlikepaladin.pfm.mixin;

import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.class)
public interface PFMSpriteAccessor {
    @Accessor("x")
    int pfm$getX();

    @Accessor("y")
    int pfm$getY();
}
