package com.unlikepaladin.pfm.blocks.models.classicDesk;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
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
public class UnbakedClassicDeskModel implements UnbakedModel {
    public static final ResourceLocation[] BASIC_MODEL_PARTS_BASE = {
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_base"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east_leg"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west_leg"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_east_leg"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_west_leg"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east_north"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east_south"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west_north"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west_south"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_east_corner"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_west_corner"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_all"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_all"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_middle_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_middle_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_right_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_right_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_left_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_left_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_mirrored_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_mirrored_open"),

            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_mirrored_closed"),
            new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_mirrored_open"),
    };
    private static final ResourceLocation PARENT = new ResourceLocation("block/block");
    public static final ResourceLocation TABLE_MODEL_ID = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/desk_classic");
    public static final List<ResourceLocation> MODEL_IDS = new ArrayList<Identifier>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_classic"));
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_classic"));
                if (variant.hasStripped()) {
                    add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_desk_classic"));
                    add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_desk_cabinet_classic"));
                }
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_classic"));
                add(new ResourceLocation(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_classic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.singleton(PARENT);
    }
    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    public static final Map<ModelState, List<BakedModel>> CACHED_MODELS = new ConcurrentHashMap<>();
    @Nullable
    @Override
    public BakedModel bake(ModelBakery loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
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
