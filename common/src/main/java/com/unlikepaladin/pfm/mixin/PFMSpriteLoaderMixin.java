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

@Mixin(SpriteLoader.class)
public class PFMSpriteLoaderMixin {

    @Unique
    private static final List<SpriteContents> pfm$injectedSprites = new ArrayList<>();

    @Inject(method = "method_47662", at = @At("HEAD"))
    private static void pfm$InjectAdditionalSprites(List<SpriteContents> sprites, CallbackInfoReturnable<List<SpriteContents>> cir,
                                                    @Local(argsOnly = true) LocalRef<List<SpriteContents>> spriteList) {
        if (!pfm$injectedSprites.isEmpty()) {
            List<SpriteContents> spriteContents = new ArrayList<>(sprites);
            spriteContents.addAll(pfm$injectedSprites);
            spriteList.set(spriteContents);
            pfm$injectedSprites.clear();
        }
    }

    @Inject(method = "method_52850", at = @At(value = "RETURN"))
    private static void pfm$AdditionalSources(Function<SpriteOpener, SpriteContents> function, SpriteOpener spriteOpener, CallbackInfoReturnable<SpriteContents> cir) {
        SpriteContents contents = cir.getReturnValue();
        if (PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(contents.getId())) {
            PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(contents.getId()).apply(contents).forEach(variantInfo -> {
                pfm$injectedSprites.add(variantInfo.getRight());
            });
        }
    }
}
