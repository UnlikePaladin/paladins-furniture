package com.unlikepaladin.pfm.blocks.models.logStool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record UnbakedLogStoolModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedLogStoolModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedLogStoolModel::variant))
                            .apply(instance, UnbakedLogStoolModel::new));

    public static final Codec<UnbakedLogStoolModel> CODEC = MAP_CODEC.codec();

    public static final ResourceLocation[] LOG_STOOL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/log_stool/log_stool"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/log_stool/log_stool_tucked")
    };

    public static final ResourceLocation STOOL_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/log_stool");
    public static final List<ResourceLocation> LOG_STOOL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_" + logType + "_stool"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_" + logType + "_stool"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_log_stool"));
            }
            add(STOOL_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(LOG_STOOL_PARTS_BASE[0]), baker.getModel(LOG_STOOL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(STOOL_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : LOG_STOOL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : LOG_STOOL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
