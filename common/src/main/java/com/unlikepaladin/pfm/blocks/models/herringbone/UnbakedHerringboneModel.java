package com.unlikepaladin.pfm.blocks.models.herringbone;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
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
import java.util.function.Function;

public class UnbakedHerringboneModel implements UnbakedModel {

    private static final List<ResourceLocation> TEMPLATE_MODEL = List.of(ResourceLocation.parse("minecraft:block/cube_all"));
    private final ResourceLocation id;
    public UnbakedHerringboneModel(ResourceLocation id) {
        this.id = id;
    }

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/herringbone_planks");
    public static final List<ResourceLocation> MODEL_IDS = new ArrayList<>() {
        {
            add(ID);
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_herringbone_planks"));
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getSerializedName() + "_herringbone_planks"));
            }
        }
    };


    @Override
    public Collection<ResourceLocation> getDependencies() {
        return TEMPLATE_MODEL;
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelLoader) {

    }

    @Override
    public @Nullable BakedModel bake(ModelBaker loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(id) && PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(id, rotationContainer, PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(id))
            PFMRuntimeResources.modelCacheMap.put(id, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : TEMPLATE_MODEL) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(id, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ResourceLocation modelId, ModelState settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
