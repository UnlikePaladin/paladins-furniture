package com.unlikepaladin.pfm.blocks.models.kitchenCounterOven;

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
public record UnbakedKitchenCounterOvenModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenCounterOvenModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedKitchenCounterOvenModel::variant))
                            .apply(instance, UnbakedKitchenCounterOvenModel::new));

    public static final Codec<UnbakedKitchenCounterOvenModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] OVEN_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_middle"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven/kitchen_counter_oven_middle_open")
    };


    public static final Identifier OVEN_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter_oven");
    public static final List<Identifier> OVEN_MODEL_IDS  = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_counter_oven"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_kitchen_counter_oven"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_counter_oven"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_counter_oven"));
            }
            add(OVEN_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(OVEN_MODEL_PARTS_BASE[0]), baker.getModel(OVEN_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(OVEN_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(OVEN_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(OVEN_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(OVEN_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : OVEN_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(OVEN_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(OVEN_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : OVEN_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}