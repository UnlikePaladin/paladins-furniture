package com.unlikepaladin.pfm.blocks.models.kitchenWallCounter;

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
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedKitchenWallCounterModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenWallCounterModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedKitchenWallCounterModel::variant))
                            .apply(instance, UnbakedKitchenWallCounterModel::new));

    public static final Codec<UnbakedKitchenWallCounterModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] COUNTER_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_inner_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_inner_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_outer_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_outer_corner_right")
    };

    public static final Identifier COUNTER_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_wall_counter");
    public static final List<Identifier> COUNTER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_wall_counter"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_kitchen_wall_counter"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_wall_counter"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_wall_counter"));
            }
            add(COUNTER_MODEL_ID);
        }
    };

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(COUNTER_MODEL_PARTS_BASE[0]), baker.getModel(COUNTER_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(COUNTER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(COUNTER_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(COUNTER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(COUNTER_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : COUNTER_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(COUNTER_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : COUNTER_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}