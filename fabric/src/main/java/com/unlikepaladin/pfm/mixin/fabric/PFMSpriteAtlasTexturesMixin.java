package com.unlikepaladin.pfm.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import net.minecraft.client.texture.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(SpriteAtlasTexture.class)
public class PFMSpriteAtlasTexturesMixin implements PFMSpriteAtlasTexturesExtensions {

    @ModifyArg(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureStitcher;add(Lnet/minecraft/client/texture/Sprite$Info;)V"))
    public Sprite.Info stitch(Sprite.Info info, @Local TextureStitcher stitcher) {
        Identifier id = info.getId();
        if (PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(id)) {
            List<Sprite.Info> generatedInfos = PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(id).apply(info);
            for (Sprite.Info generatedInfo : generatedInfos) {
                stitcher.add(generatedInfo);
                PFMSpriteRegistry.PFM_SPRITE_COORDINATES.put(generatedInfo.getId(), null);
            }
        }
        return info;
    }

    @Inject(method = "loadSprite", at = @At(value = "HEAD"))
    public void saveSpriteProperties(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> cir) {
        if (PFMSpriteRegistry.PFM_SPRITE_COORDINATES.containsKey(info.getId())) {
            PFMSpriteRegistry.PFM_SPRITE_COORDINATES.put(info.getId(), new TextureReloadQueue.SpriteCoordinates(x, y, info.getWidth(), info.getHeight(), atlasWidth, atlasHeight));
        }
    }

    @Inject(method = "loadSprite", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void cancelErrorForPFMTextures(ResourceManager container, Sprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<Sprite> cir) {
        if (PFMSpriteRegistry.PFM_SPRITE_COORDINATES.containsKey(info.getId())) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "loadSprites(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/client/texture/TextureStitcher;I)Ljava/util/List;", at = @At(value = "HEAD"), cancellable = true)
    public void cancelErrorForPFMTextures(ResourceManager arg, TextureStitcher arg2, int maxLevel, CallbackInfoReturnable<List<Sprite>> cir) {
        pfm$maxLevel = maxLevel;
    }

    @Unique
    Integer pfm$maxLevel = null;

    @Override
    public Integer pfm$getMaxLevel() {
        return pfm$maxLevel;
    }
}
