package com.unlikepaladin.pfm.blocks.models.herringbone;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;

import java.util.*;

public record UnbakedHerringboneModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {

    public static final MapCodec<UnbakedHerringboneModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedHerringboneModel::variant))
                            .apply(instance, UnbakedHerringboneModel::new));

    public static final Codec<UnbakedHerringboneModel> CODEC = MAP_CODEC.codec();

    private static final List<Identifier> TEMPLATE_MODEL = List.of(Identifier.of("minecraft:block/block"), Identifier.of("minecraft:block/cube_all"));

    public static final Identifier ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/herringbone_planks");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            add(ID);
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_herringbone_planks"));
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_herringbone_planks"));
            }
        }
    };

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier modelPart : TEMPLATE_MODEL) {
            resolver.markDependency(modelPart);
        }
    }

    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        BakedSimpleModel model = baker.getModel(TEMPLATE_MODEL.getFirst());
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, model, model.getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(ID) && PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(ID))
            PFMRuntimeResources.modelCacheMap.put(ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : TEMPLATE_MODEL) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
