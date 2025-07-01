package com.unlikepaladin.pfm.blocks.models.basicLamp;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

public record UnbakedBasicLampModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBasicLampModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedBasicLampModel::variant))
                            .apply(instance, UnbakedBasicLampModel::new));

    public static final Codec<UnbakedBasicLampModel> CODEC = MAP_CODEC.codec();

    public static final List<Identifier> LAMP_MODEL_IDS = new ArrayList<>() {
        {
            add(LAMP_MODEL_ID);
            add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/basic_lamp"));
        }
    };

    public static final Identifier LAMP_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/basic_lamp");

    public static Identifier getItemModelId() {
        return LAMP_MODEL_IDS.get(1);
    }

    public static final List<String> MODEL_PARTS_BASE = new ArrayList<>() {{
       add("block/basic_lamp/basic_lamp_bottom");
       add("block/basic_lamp/basic_lamp_middle");
       add("block/basic_lamp/basic_lamp_single");
        add("block/basic_lamp/basic_lamp_top");
    }};

    public static final List<String> STATIC_PARTS = new ArrayList<>() {{
        add("block/basic_lamp/basic_lamp_shade");
        add("block/basic_lamp/basic_lamp_light_bulb_off");
        add("block/basic_lamp/basic_lamp_light_bulb_on");
    }};

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for (String part : MODEL_PARTS_BASE) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : STATIC_PARTS) {
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(Identifier.of(PaladinFurnitureMod.MOD_ID, MODEL_PARTS_BASE.get(2))), baker.getModel(Identifier.of(PaladinFurnitureMod.MOD_ID, MODEL_PARTS_BASE.get(2))).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(LAMP_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(LAMP_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(LAMP_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(LAMP_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(LAMP_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(LAMP_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : ALL_MODEL_IDS) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(LAMP_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(LAMP_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : ALL_MODEL_IDS)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}
