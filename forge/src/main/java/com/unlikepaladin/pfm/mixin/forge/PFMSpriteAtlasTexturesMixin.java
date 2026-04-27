package com.unlikepaladin.pfm.mixin.forge;

import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteAtlasTexturesExtensions;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
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

@Mixin(TextureAtlas.class)
public class PFMSpriteAtlasTexturesMixin implements PFMSpriteAtlasTexturesExtensions {

    @Unique
    Stitcher pfm$stitcher;

    @Inject(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    public void beforeStitching(ResourceManager resourceManager, Stream<ResourceLocation> idStream, ProfilerFiller profiler, int mipmapLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, Set<ResourceLocation> set, int i, Stitcher stitcher) {
        this.pfm$stitcher = stitcher;
    }

    @Inject(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    public void beforeStitchingOF(ResourceManager resourceManager, Stream<ResourceLocation> idStream, ProfilerFiller profiler, int mipmapLevel, CallbackInfoReturnable<TextureAtlas.Preparations> cir, int mipMapLevels, Set<ResourceLocation> set, Set<ResourceLocation> emissive, int i, Stitcher stitcher) {
        this.pfm$stitcher = stitcher;
    }

    @ModifyArg(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/Stitcher;registerSprite(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;)V"))
    public TextureAtlasSprite.Info stitch(TextureAtlasSprite.Info info) {
        ResourceLocation id = info.name();
        if (PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(id)) {
            List<TextureAtlasSprite.Info> generatedInfos = PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(id).apply(info);
            for (TextureAtlasSprite.Info generatedInfo : generatedInfos) {
                pfm$stitcher.registerSprite(generatedInfo);
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

    @Inject(method = "load(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite$Info;IIIII)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void cancelErrorForPFMTextures(ResourceManager container, TextureAtlasSprite.Info info, int atlasWidth, int atlasHeight, int maxLevel, int x, int y, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (PFMSpriteRegistry.PFM_SPRITE_COORDINATES.containsKey(info.name())) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getLoadedSprites(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/renderer/texture/Stitcher;I)Ljava/util/List;", at = @At(value = "HEAD"), cancellable = true)
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
