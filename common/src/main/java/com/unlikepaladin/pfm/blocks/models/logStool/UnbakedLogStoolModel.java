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
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

public record UnbakedLogStoolModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedLogStoolModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedLogStoolModel::variant))
                            .apply(instance, UnbakedLogStoolModel::new));

    public static final Codec<UnbakedLogStoolModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] LOG_STOOL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_stool/log_stool"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_stool/log_stool_tucked")
    };

    public static final Identifier STOOL_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_stool");
    public static final List<Identifier> LOG_STOOL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_" + logType + "_stool"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_" + logType + "_stool"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_log_stool"));
            }
            add(STOOL_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(LOG_STOOL_PARTS_BASE[0]), baker.getModel(LOG_STOOL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(STOOL_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : LOG_STOOL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : LOG_STOOL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}
