package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PFMSpriteRegistry {
    public static ResourceLocation HERRINGBONE_PLANKS = ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/template_herringbone_planks");
    public static Map<ResourceLocation, Function<SpriteContents, List<Tuple<ResourceLocation, SpriteContents>>>> DYNAMIC_SPRITE_GENERATORS = new HashMap<>();

    public static void registerAdditionalSprites() {
        if (!DYNAMIC_SPRITE_GENERATORS.isEmpty())
            return;

        List<VariantBase<?>> variantBaseList = new ArrayList<>(WoodVariantRegistry.getVariants());

        // herringbone
        registerDynamicSprite(HERRINGBONE_PLANKS, variantBaseList);

    }

    @ExpectPlatform
    public static void registerSprite(ResourceLocation spriteId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerDynamicSprite(ResourceLocation spriteId, List<VariantBase<?>> variantBaseList) {
        throw new AssertionError();
    }
}
