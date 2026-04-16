package com.unlikepaladin.pfm.blocks.models.kitchenCabinet;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms; 
import net.minecraft.client.renderer.block.model.TextureSlots;
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
public class UnbakedKitchenCabinetModel implements UnbakedModel {
    public static final ResourceLocation[] CABINET_MODEL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_open"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_open_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_open_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_open_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_open_right")
    };

    private static final ResourceLocation PARENT = ResourceLocation.parse("block/block");
    public static final ResourceLocation CABINET_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet");
    public static final List<ResourceLocation> CABINET_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_cabinet"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_cabinet"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_cabinet"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_cabinet"));
            }
            add(CABINET_MODEL_ID);
        }
    };

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(TextureSlots textures, ModelBaker loader, ModelState rotationContainer, boolean ambientOcclusion, boolean isSideLit, ItemTransforms transformation){
        if (PFMRuntimeResources.modelCacheMap.containsKey(CABINET_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(CABINET_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(CABINET_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(CABINET_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : CABINET_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(CABINET_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : CABINET_MODEL_PARTS_BASE)
            resolver.resolve(c);
    }
}