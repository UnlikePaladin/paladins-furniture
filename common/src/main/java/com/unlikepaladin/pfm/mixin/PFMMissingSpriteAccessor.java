package com.unlikepaladin.pfm.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MissingTextureAtlasSprite.class)
public interface PFMMissingSpriteAccessor {
    @Invoker("generateMissingImage")
    static NativeImage pfm$invokeCreateImage(int width, int height) {
        throw new AssertionError();
    }
}
