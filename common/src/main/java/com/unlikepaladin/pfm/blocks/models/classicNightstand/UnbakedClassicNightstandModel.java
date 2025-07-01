package com.unlikepaladin.pfm.blocks.models.classicNightstand;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.UnbakedClassicCoffeeTableModel;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

public record UnbakedClassicNightstandModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedClassicNightstandModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedClassicNightstandModel::variant))
                            .apply(instance, UnbakedClassicNightstandModel::new));

    public static final Codec<UnbakedClassicNightstandModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] NIGHTSTAND_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_open")
    };

    public static final Identifier NIGHTSTAND_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand");
    public static final List<Identifier> NIGHSTAND_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_classic_nightstand"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
            }
            add(NIGHTSTAND_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(NIGHTSTAND_MODEL_PARTS_BASE[3]), baker.getModel(NIGHTSTAND_MODEL_PARTS_BASE[3]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(NIGHTSTAND_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(NIGHTSTAND_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : NIGHTSTAND_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(NIGHTSTAND_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : NIGHTSTAND_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}
