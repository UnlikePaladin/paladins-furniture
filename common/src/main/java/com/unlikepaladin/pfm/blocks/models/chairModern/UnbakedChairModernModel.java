package com.unlikepaladin.pfm.blocks.models.chairModern;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UnbakedChairModernModel implements UnbakedModel {
    public static final ResourceLocation[] CHAIR_MODERN_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_modern/chair_modern"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_modern/chair_modern_tucked")
    };

    public static final ResourceLocation CHAIR_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_modern");
    public static final List<ResourceLocation> CHAIR_MODERN_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){

                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_chair_modern"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_chair_modern"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_chair_modern"));
            }
            add(CHAIR_MODEL_ID);
        }
    };

    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public Collection<ResourceLocation> getDependencies() {
        return List.of(PARENT);
    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(CHAIR_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(CHAIR_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : CHAIR_MODERN_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(CHAIR_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (ResourceLocation c : CHAIR_MODERN_PARTS_BASE)
            resolver.resolve(c);
    }
}
