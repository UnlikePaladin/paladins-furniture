package com.unlikepaladin.pfm.blocks.models.chairDinner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
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

public record UnbakedChairDinnerModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedChairDinnerModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedChairDinnerModel::variant))
                            .apply(instance, UnbakedChairDinnerModel::new));

    public static final Codec<UnbakedChairDinnerModel> CODEC = MAP_CODEC.codec();

    public static final ResourceLocation[] CHAIR_DINNER_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_dinner/chair_dinner"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_dinner/chair_dinner_tucked")
    };

    public static final ResourceLocation CHAIR_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/chair_dinner");
    public static final List<ResourceLocation> CHAIR_DINNER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){

                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_chair_dinner"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_chair_dinner"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){

                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_chair_dinner"));
            }
            add(CHAIR_MODEL_ID);
        }
    };

    @Nullable
    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(CHAIR_DINNER_PARTS_BASE[0]), baker.getModel(CHAIR_DINNER_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(CHAIR_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(CHAIR_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : CHAIR_DINNER_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(CHAIR_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation id : CHAIR_DINNER_PARTS_BASE) {
            resolver.markDependency(id);
        }
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
