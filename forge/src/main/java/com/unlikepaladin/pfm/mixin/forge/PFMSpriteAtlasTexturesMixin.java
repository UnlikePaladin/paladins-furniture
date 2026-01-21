package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureStitcher;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(SpriteAtlasTexture.class)
public class PFMSpriteAtlasTexturesMixin implements PFMSpriteAtlasTexturesExtensions {

    @Unique
    TextureStitcher pfm$stitcher;

    @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    public void beforeStitching(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler, int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir, Set<Identifier> set, int i, TextureStitcher stitcher) {
        this.pfm$stitcher = stitcher;
    }

    @Inject(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    public void beforeStitchingOF(ResourceManager resourceManager, Stream<Identifier> idStream, Profiler profiler, int mipmapLevel, CallbackInfoReturnable<SpriteAtlasTexture.Data> cir, int mipMapLevels, Set<Identifier> set, Set<Identifier> emissive, int i, TextureStitcher stitcher) {
        this.pfm$stitcher = stitcher;
    }

    @ModifyArg(method = "stitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureStitcher;add(Lnet/minecraft/client/texture/Sprite$Info;)V"))
    public Sprite.Info stitch(Sprite.Info info) {
        Identifier id = info.getId();
        if (PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(id)) {
            List<Sprite.Info> generatedInfos = PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(id).apply(info);
            for (Sprite.Info generatedInfo : generatedInfos) {
                pfm$stitcher.add(generatedInfo);
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
