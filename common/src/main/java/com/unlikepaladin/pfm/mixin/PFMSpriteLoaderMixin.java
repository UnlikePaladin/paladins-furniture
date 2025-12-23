package com.unlikepaladin.pfm.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.SingleAtlasSource;
import net.minecraft.client.texture.atlas.UnstitchAtlasSource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(SpriteLoader.class)
public class PFMSpriteLoaderMixin {

    @Inject(method = "method_47662", at = @At("HEAD"))
    private static void pfm$InjectAdditionalSprites(List<SpriteContents> sprites, CallbackInfoReturnable<List<SpriteContents>> cir,
                                                    @Local(argsOnly = true) LocalRef<List<SpriteContents>> spriteList) {

        List<SpriteContents> spritesCopy = new ArrayList<>(sprites);

        List<SpriteContents> matchingContents = sprites.stream()
            .filter(sc -> {
                try {
                    return PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(sc.getId());
                } catch (RuntimeException e) {
                    return false;
                }
            })
            .toList();

        for (SpriteContents sc : matchingContents) {
            spritesCopy.addAll(PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(sc.getId()).apply(sc).stream().map(Pair::getRight).toList());
        }

        spriteList.set(spritesCopy);
    }

}
