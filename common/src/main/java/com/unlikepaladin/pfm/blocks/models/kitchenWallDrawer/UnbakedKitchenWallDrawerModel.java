package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer;

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
public class UnbakedKitchenWallDrawerModel implements UnbakedModel {
    public static final ResourceLocation[] COUNTER_MODEL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_open"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_open_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_open_right")
    };


    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public static final ResourceLocation DRAWER_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_wall_drawer");
    public static final List<ResourceLocation> DRAWER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            add(DRAWER_MODEL_ID);
        }
    };

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(DRAWER_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(DRAWER_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : COUNTER_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(DRAWER_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : COUNTER_MODEL_PARTS_BASE)
            resolver.resolve(c);
    }
}