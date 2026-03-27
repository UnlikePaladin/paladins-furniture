package com.unlikepaladin.pfm.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(TextureAtlas.class)
public class PFMSpriteAtlasTexturesMixin implements PFMSpriteAtlasTexturesExtensions {

    @ModifyArg(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/Stitcher;registerSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;)V"))
    public TextureAtlasSprite.Info stitch(TextureAtlasSprite.Info info, @Local Stitcher stitcher) {
        ResourceLocation id = info.name();
        if (PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(id)) {
            List<TextureAtlasSprite.Info> generatedInfos = PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(id).apply(info);
            for (TextureAtlasSprite.Info generatedInfo : generatedInfos) {
                stitcher.registerSprite(generatedInfo);
                PFMSpriteRegistry.PFM_SPRITE_COORDINATES.put(generatedInfo.name(), null);
            }
        }
        return info;
    }

    @Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At(value = "HEAD"))
    public void saveSpriteProperties(ResourceManager container, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (PFMSpriteRegistry.PFM_SPRITE_COORDINATES.containsKey(info.name())) {
            PFMSpriteRegistry.PFM_SPRITE_COORDINATES.put(info.name(), new TextureReloadQueue.SpriteCoordinates(x, y, info.width(), info.height(), atlasWidth, atlasHeight));
        }
    }

    @Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void cancelErrorForPFMTextures(ResourceManager container, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (PFMSpriteRegistry.PFM_SPRITE_COORDINATES.containsKey(info.name())) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getLoadedSprites", at = @At(value = "HEAD"), cancellable = true)
    public void cancelErrorForPFMTextures(ResourceManager arg, Stitcher arg2, int maxLevel, CallbackInfoReturnable<List<TextureAtlasSprite>> cir) {
        pfm$maxLevel = maxLevel;
    }

    @Unique
    Integer pfm$maxLevel = null;

    @Override
    public Integer pfm$getMaxLevel() {
        return pfm$maxLevel;
    }
}
