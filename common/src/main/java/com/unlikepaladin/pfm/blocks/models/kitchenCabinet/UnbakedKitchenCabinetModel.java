package com.unlikepaladin.pfm.blocks.models.kitchenCabinet;

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
public record UnbakedKitchenCabinetModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenCabinetModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedKitchenCabinetModel::variant))
                            .apply(instance, UnbakedKitchenCabinetModel::new));

    public static final Codec<UnbakedKitchenCabinetModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] CABINET_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_open_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_inner_corner_open_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_open_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/kitchen_cabinet_outer_corner_open_right")
    };

    private static final Identifier PARENT = Identifier.of("block/block");
    public static final Identifier CABINET_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet");
    public static final List<Identifier> CABINET_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_cabinet"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_kitchen_cabinet"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_cabinet"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_cabinet"));
            }
            add(CABINET_MODEL_ID);
        }
    };

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(CABINET_MODEL_PARTS_BASE[0]), baker.getModel(CABINET_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(CABINET_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(CABINET_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(CABINET_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(CABINET_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : CABINET_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(CABINET_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(CABINET_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : CABINET_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}