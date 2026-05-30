package com.unlikepaladin.pfm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(SpriteLoader.class)
public class PFMSpriteLoaderMixin {

    @Inject(method = "method_47662", at = @At("HEAD"))
    private static void pfm$InjectAdditionalSprites(List<SpriteContents> sprites, CallbackInfoReturnable<List<SpriteContents>> cir,
                                                    @Local(argsOnly = true) LocalRef<List<SpriteContents>> spriteList) {

        PFMSpriteRegistry.registerAdditionalSprites();
        List<SpriteContents> spritesCopy = new ArrayList<>(sprites);

        Set<Identifier> ids = sprites.stream().map(SpriteContents::name).collect(Collectors.toSet());
        List<SpriteContents> matchingContents = sprites.stream()
            .filter(sc -> {
                try {
                    return PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(sc.name());
                } catch (RuntimeException e) {
                    return false;
                }
            })
            .toList();

        for (SpriteContents sc : matchingContents) {
            spritesCopy.addAll(PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(sc.name()).apply(sc).stream()
                    .filter(a -> !ids.contains(a.getA())).map(Tuple::getB).toList());
        }

        spriteList.set(spritesCopy);
    }

}
