package com.unlikepaladin.pfm.blocks.models.bed;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.basicTable.UnbakedBasicTableModel;
import com.unlikepaladin.pfm.blocks.models.chairClassic.UnbakedChairClassicModel;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedBedModel(ModelVariant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBedModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(ModelVariant.MAP_CODEC.forGetter(UnbakedBedModel::variant))
                            .apply(instance, UnbakedBedModel::new));

    public static final Codec<UnbakedBedModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] BED_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_foot_mattress"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_head_mattress"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/full/simple_bed"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_foot_mattress"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_head_mattress"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_right"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_left"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            Identifier.of(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/full/classic_bed"),
    };

    public static final Identifier BED_MODEL_ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/simple_bed");
    public static final List<Identifier> BED_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_" + dyeColor.getId() + "_simple_bed"));
                    i++;
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_" + dyeColor.getId() + "_classic_bed"));
                    i++;
                }
            }
            add(BED_MODEL_ID);
        }
    };

    public static Pair<BlockModelPart, BlockModelPart> inventoryModels = new Pair<>(null,null);
    @Override
    public BlockStateModel bake(Baker baker){
        ModelBakeSettings settings = variant.modelState().asModelBakeSettings();
        ModelSettings itemSettings = ModelSettings.resolveSettings(baker, baker.getModel(BED_MODEL_PARTS_BASE[0]), baker.getModel(BED_MODEL_PARTS_BASE[0]).getTextures());

        if (PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(BED_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(BED_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BED_MODEL_PARTS_BASE) {
            BlockModelPart model = GeometryBakedModel.create(baker, modelPart, settings);
            bakedModelList.add(model);
            if (modelPart.getPath().contains("full")) {
                if (modelPart.getPath().contains("simple"))
                    inventoryModels.setLeft(model);
                else
                    inventoryModels.setRight(model);
            }
        }

        PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(BED_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier model, ModelBakeSettings settings, ModelSettings itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier c : BED_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> codec() {
        return MAP_CODEC;
    }
}