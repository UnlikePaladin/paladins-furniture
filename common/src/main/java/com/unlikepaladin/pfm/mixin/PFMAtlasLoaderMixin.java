package com.unlikepaladin.pfm.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.atlas.AtlasLoader;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.SingleAtlasSource;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(AtlasLoader.class)
public class PFMAtlasLoaderMixin {

    @Unique
    Identifier pfm$atlasId = null;

    @Shadow
    @Final
    private List<AtlasSource> sources;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void pfm$Init(List<AtlasSource> sources, CallbackInfo ci) {
        for (Map.Entry<Identifier, Identifier> entry : PFMSpriteRegistry.PFM_SPRITES.entrySet().stream()
                .filter(identifierIdentifierEntry -> identifierIdentifierEntry.getKey() == pfm$atlasId).toList()) {
            sources.add(new SingleAtlasSource(entry.getValue(), Optional.empty()));
        }
    }
    @Inject(method = "loadSources", at = @At(value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableList$Builder;addAll(Ljava/lang/Iterable;)Lcom/google/common/collect/ImmutableList$Builder;", shift = At.Shift.AFTER))
    public void pfm$AdditionalSources(CallbackInfoReturnable<List<Supplier<SpriteContents>>> cir, @Local Map<Identifier, AtlasSource.SpriteRegion> map, @Local ImmutableList.Builder<Supplier<SpriteContents>> builder) {
        List<Supplier<SpriteContents>> suppliers = new ArrayList<>(map.values());
        if (suppliers.isEmpty()) {
            return;
        }

        List<Supplier<SpriteContents>> matchingSuppliers = suppliers.stream()
            .filter(supplier -> {
                try {
                    SpriteContents sc = supplier.get();
                    return PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.containsKey(sc.getId());
                } catch (RuntimeException e) {
                    return false;
                }
            })
            .toList();

        List<SpriteContents> matchingContents = matchingSuppliers.stream()
            .map(Supplier::get)
            .toList();

        for (SpriteContents info : matchingContents) {
            PFMSpriteRegistry.DYNAMIC_SPRITE_GENERATORS.get(info.getId()).apply(info).forEach(variantInfo -> {
                map.put(variantInfo.getLeft(), variantInfo::getRight);
                builder.add(variantInfo::getRight);
            });
        }

    }

    @Inject(method = "of", at = @At("RETURN"))
    private static void pfm$storeAtlasId(ResourceManager resourceManager, Identifier id, CallbackInfoReturnable<AtlasLoader> cir) {
        ((PFMAtlasLoaderMixin)(Object)cir.getReturnValue()).pfm$atlasId = id;
    }
}
