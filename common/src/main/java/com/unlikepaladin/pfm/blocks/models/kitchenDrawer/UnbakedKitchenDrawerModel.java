package com.unlikepaladin.pfm.blocks.models.kitchenDrawer;

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
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedKitchenDrawerModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenDrawerModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedKitchenDrawerModel::variant))
                            .apply(instance, UnbakedKitchenDrawerModel::new));

    public static final Codec<UnbakedKitchenDrawerModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] COUNTER_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_edge_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_edge_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_inner_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_inner_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_outer_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_outer_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_edge_left_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_edge_right_open"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_inner_corner_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_inner_corner_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_outer_corner_open_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_outer_corner_open_right")
    };

    public static final Identifier DRAWER_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer");
    public static final List<Identifier> DRAWER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_drawer"));
                if (variant.hasStripped())
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_kitchen_drawer"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_drawer"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_drawer"));
            }
            add(DRAWER_MODEL_ID);
        }
    };

    @Nullable
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(COUNTER_MODEL_PARTS_BASE[0]), baker.getModel(COUNTER_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(DRAWER_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(DRAWER_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : COUNTER_MODEL_PARTS_BASE) {
            bakedModelList.add(GeometryBakedModel.create(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(DRAWER_MODEL_ID, settings, itemSettings, bakedModelList);
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
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}