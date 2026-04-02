package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class PFMSpriteAtlasTextureMixin implements PFMSpriteAtlasTexturesExtensions {
    @Inject(method = "upload", at = @At(value = "HEAD"))
    public void saveMipLevel(SpriteLoader.Preparations stitchResult, CallbackInfo ci) {
        pfm$maxLevel = stitchResult.mipLevel();
    }

    @Unique
    Integer pfm$maxLevel = null;

    @Override
    public Integer pfm$getMaxLevel() {
        return pfm$maxLevel;
    }
}
