package com.unlikepaladin.pfm.blocks.models.modernDinnerTable;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedModernDinnerTableModel implements UnbakedModel {
    public static final ResourceLocation[] MODERN_DINNER_MODEL_PARTS_BASE = new ResourceLocation[] {
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_modern_dinner/table_modern_dinner_base"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_modern_dinner/table_modern_dinner_right"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_modern_dinner/table_modern_dinner_left"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_modern_dinner/table_modern_dinner_legs"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_modern_dinner/table_modern_dinner_middle"),
    };


    private static final ResourceLocation PARENT = new ResourceLocation("block/block");
    public static final ResourceLocation TABLE_MODEL_ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/modern_dinner_table");
    public static final List<ResourceLocation> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_modern_dinner"));
                if (variant.hasStripped())
                    add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_table_modern_dinner"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_modern_dinner"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of(PARENT);
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(TABLE_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : MODERN_DINNER_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}