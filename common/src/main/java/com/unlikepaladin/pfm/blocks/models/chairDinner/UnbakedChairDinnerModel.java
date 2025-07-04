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
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record UnbakedChairDinnerModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedChairDinnerModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedChairDinnerModel::variant))
                            .apply(instance, UnbakedChairDinnerModel::new));

    public static final Codec<UnbakedChairDinnerModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] CHAIR_DINNER_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/chair_dinner/chair_dinner"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/chair_dinner/chair_dinner_tucked")
    };

    public static final Identifier CHAIR_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/chair_dinner");
    public static final List<Identifier> CHAIR_DINNER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){

                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_chair_dinner"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_chair_dinner"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){

                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_chair_dinner"));
            }
            add(CHAIR_MODEL_ID);
        }
    };

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(CHAIR_DINNER_PARTS_BASE[0]), baker.getModel(CHAIR_DINNER_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(CHAIR_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(CHAIR_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(CHAIR_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : CHAIR_DINNER_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(CHAIR_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(CHAIR_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier id : CHAIR_DINNER_PARTS_BASE) {
            resolver.markDependency(id);
        }
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
