package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class UnbakedKitchenCounterOvenModel implements UnbakedModel {

    public static final ResourceLocation[] OVEN_MODEL_PARTS_BASE = new ResourceLocation[] {
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_middle"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_open"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_middle_open")
    };


    private static final ResourceLocation PARENT = new ResourceLocation("block/block");
    public static final ResourceLocation OVEN_MODEL_ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven");
    public static final List<ResourceLocation> OVEN_MODEL_IDS  = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_counter_oven"));
                if (variant.hasStripped())
                    add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_counter_oven"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_counter_oven"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_counter_oven"));
            }
            add(OVEN_MODEL_ID);
        }
    };

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of(PARENT);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {

    }

    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(OVEN_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(OVEN_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(OVEN_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(OVEN_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : OVEN_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(OVEN_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}