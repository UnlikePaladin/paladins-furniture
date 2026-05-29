package com.unlikepaladin.pfm.blocks.models.ladder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.kitchenWallDrawerSmall.UnbakedKitchenWallDrawerSmallModel;
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

public record UnbakedLadderModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedLadderModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedLadderModel::variant))
                            .apply(instance, UnbakedLadderModel::new));

    public static final Codec<UnbakedLadderModel> CODEC = MAP_CODEC.codec();

    public static final ResourceLocation[] LADDER_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bunk_ladder/simple_ladder"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bunk_ladder/simple_ladder_top")
    };

    public static final ResourceLocation LADDER_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bunk_ladder");
    public static final List<ResourceLocation> LADDER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_simple_bunk_ladder"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_simple_bunk_ladder"));
            }
            add(LADDER_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(LADDER_PARTS_BASE[0]), baker.getModel(LADDER_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(LADDER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(LADDER_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(LADDER_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(LADDER_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(LADDER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(LADDER_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : LADDER_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(LADDER_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(LADDER_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : LADDER_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
