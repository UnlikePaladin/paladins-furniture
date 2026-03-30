package com.unlikepaladin.pfm.blocks.models.basicTable;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedBasicTableModel implements UnbakedModel {
    public static final ResourceLocation[] BASIC_MODEL_PARTS_BASE = {
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_base"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_top"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_top"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_north"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_south"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_bottom"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_bottom"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west_corner")
    };
    private static final ResourceLocation PARENT = new ResourceLocation("block/block");
    public static final ResourceLocation TABLE_MODEL_ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/table_basic");
    public static final List<ResourceLocation> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_basic"));
                if (variant.hasStripped())
                    add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_table_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_basic"));
            }
            add(TABLE_MODEL_ID);
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
        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(TABLE_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : BASIC_MODEL_PARTS_BASE) {
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
