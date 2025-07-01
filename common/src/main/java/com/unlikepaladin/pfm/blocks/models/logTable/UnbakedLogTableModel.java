package com.unlikepaladin.pfm.blocks.models.logTable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedLogTableModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedLogTableModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedLogTableModel::variant))
                            .apply(instance, UnbakedLogTableModel::new));

    public static final Codec<UnbakedLogTableModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] LOG_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_table/log_table_middle"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_table/log_table_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_table/log_table_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_table/log_table_legs")
    };

    public static final Identifier TABLE_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/log_table");
    public static final List<Identifier> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_table_" + logType));
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_raw_table_" + logType));
                if (variant.hasStripped()) {
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_table_" + logType));
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_raw_table_" + logType));
                }
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_table_natural"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(LOG_MODEL_PARTS_BASE[0]), baker.getModel(LOG_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : LOG_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : LOG_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}