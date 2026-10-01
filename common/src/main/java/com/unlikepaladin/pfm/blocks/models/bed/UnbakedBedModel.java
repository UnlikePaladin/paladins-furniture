package com.unlikepaladin.pfm.blocks.models.bed;

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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import com.unlikepaladin.pfm.utilities.Tuple;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedBedModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBedModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedBedModel::variant))
                            .apply(instance, UnbakedBedModel::new));

    public static final Codec<UnbakedBedModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] BED_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_foot_mattress"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/mattresses/red_head_mattress"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/foot/simple_bed_foot_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/head/simple_bed_head_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/foot/simple_bed_foot_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/full/simple_bed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_foot_mattress"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/mattresses/red_head_mattress"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/foot/classic_bed_foot_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/head/classic_bed_head_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/bunk/foot/classic_bed_foot_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed/template/bunk/head/simple_bed_head"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_bed/template/full/classic_bed"),
    };

    public static final Identifier BED_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/simple_bed");
    public static final List<Identifier> BED_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_" + dyeColor.getName() + "_simple_bed"));
                    i++;
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                int i = 0;
                for (DyeColor dyeColor : DyeColor.values()) {
                    if (i > 15)
                        break;
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_" + dyeColor.getName() + "_classic_bed"));
                    i++;
                }
            }
            add(BED_MODEL_ID);
        }
    };

    public static Tuple<BlockStateModelPart, BlockStateModelPart> inventoryModels = new Tuple<>(null,null);
    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(BED_MODEL_PARTS_BASE[0]), baker.getModel(BED_MODEL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(BED_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(BED_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(BED_MODEL_ID, new PFMBakedModelContainer());

        List<BlockStateModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BED_MODEL_PARTS_BASE) {
            BlockStateModelPart model = SimpleModelWrapper.bake(baker, modelPart, settings);
            bakedModelList.add(model);
            if (modelPart.getPath().contains("full")) {
                if (modelPart.getPath().contains("simple"))
                    inventoryModels.setA(model);
                else
                    inventoryModels.setB(model);
            }
        }

        PFMRuntimeResources.modelCacheMap.get(BED_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(BED_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier model, ModelState settings, ModelRenderProperties itemSettings, List<BlockStateModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : BED_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}