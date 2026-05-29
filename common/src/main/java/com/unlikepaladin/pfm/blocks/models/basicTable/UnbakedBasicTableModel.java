package com.unlikepaladin.pfm.blocks.models.basicTable;

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
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedBasicTableModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBasicTableModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedBasicTableModel::variant))
                            .apply(instance, UnbakedBasicTableModel::new));

    public static final Codec<UnbakedBasicTableModel> CODEC = MAP_CODEC.codec();

    public static final ResourceLocation[] BASIC_MODEL_PARTS_BASE = {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_base"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_top"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_top"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_north"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_south"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_bottom"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_bottom"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east_corner"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west_corner"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east_corner"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west_corner")
    };

    public static final ResourceLocation TABLE_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/table_basic");
    public static final List<ResourceLocation> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_basic"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_table_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_table_basic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(BASIC_MODEL_PARTS_BASE[2]), baker.getModel(BASIC_MODEL_PARTS_BASE[2]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : BASIC_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : BASIC_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
