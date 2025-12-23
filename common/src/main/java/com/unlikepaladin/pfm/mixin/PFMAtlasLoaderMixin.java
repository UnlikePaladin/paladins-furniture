package com.unlikepaladin.pfm.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.ducks.PFMSpriteExtensions;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.SingleAtlasSource;
import net.minecraft.client.texture.atlas.UnstitchAtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(AtlasLoader.class)
public class PFMAtlasLoaderMixin {

    @Unique
    Identifier pfm$atlasId = null;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void pfm$Init(List<AtlasSource> sources, CallbackInfo ci) {
        for (Map.Entry<Identifier, Identifier> entry : PFMSpriteRegistry.PFM_SPRITES.entrySet().stream()
                .filter(identifierIdentifierEntry -> identifierIdentifierEntry.getKey() == pfm$atlasId).toList()) {
            sources.add(new SingleAtlasSource(entry.getValue(), Optional.empty()));
        }
    }

    @Inject(method = "of", at = @At("RETURN"))
    private static void pfm$storeAtlasId(ResourceManager resourceManager, Identifier id, CallbackInfoReturnable<AtlasLoader> cir) {
        ((PFMAtlasLoaderMixin)(Object)cir.getReturnValue()).pfm$atlasId = id;
    }
}
