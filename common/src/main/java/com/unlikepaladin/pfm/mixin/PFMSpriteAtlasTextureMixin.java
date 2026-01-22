package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteAtlasTexture.class)
public class PFMSpriteAtlasTextureMixin implements PFMSpriteAtlasTexturesExtensions {
    @Inject(method = "create", at = @At(value = "HEAD"))
    public void saveMipLevel(SpriteLoader.StitchResult stitchResult, CallbackInfo ci) {
        pfm$maxLevel = stitchResult.mipLevel();
    }

    @Unique
    Integer pfm$maxLevel = null;

    @Override
    public Integer pfm$getMaxLevel() {
        return pfm$maxLevel;
    }
}
