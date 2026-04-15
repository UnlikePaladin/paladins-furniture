package com.unlikepaladin.pfm.blocks.models.classicNightstand;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UnbakedClassicNightstandModel implements UnbakedModel {
    public static final ResourceLocation[] NIGHTSTAND_MODEL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle_open"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right_open"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left_open"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_open")
    };

    public static final ResourceLocation NIGHTSTAND_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand");
    public static final List<ResourceLocation> NIGHSTAND_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_classic_nightstand"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_classic_nightstand"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_classic_nightstand"));
            }
            add(NIGHTSTAND_MODEL_ID);
        }
    };

    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public Collection<ResourceLocation> getDependencies() {
        return List.of(PARENT);
    }

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(TextureSlots textures, ModelBaker loader, ModelState rotationContainer, boolean ambientOcclusion, boolean isSideLit, ItemTransforms transformation){
        if (PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(NIGHTSTAND_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(NIGHTSTAND_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : NIGHTSTAND_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(NIGHTSTAND_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : NIGHTSTAND_MODEL_PARTS_BASE)
            resolver.resolve(c);
    }
}
