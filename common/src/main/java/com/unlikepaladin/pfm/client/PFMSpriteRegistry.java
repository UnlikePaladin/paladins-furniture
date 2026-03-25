package com.unlikepaladin.pfm.client;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.TextureReloadQueue;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PFMSpriteRegistry {
    public static ResourceLocation HERRINGBONE_PLANKS = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/template_herringbone_planks");
    public static Map<ResourceLocation, Function<TextureAtlasSprite.Info, List<TextureAtlasSprite.Info>>> DYNAMIC_SPRITE_GENERATORS = new HashMap<>();
    public static Map<ResourceLocation, TextureReloadQueue.SpriteCoordinates> PFM_SPRITE_COORDINATES = new HashMap<>();

    public static void registerAdditionalSprites() {
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
