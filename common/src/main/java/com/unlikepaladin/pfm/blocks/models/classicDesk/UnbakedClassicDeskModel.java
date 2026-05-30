package com.unlikepaladin.pfm.blocks.models.classicDesk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public record UnbakedClassicDeskModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedClassicDeskModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedClassicDeskModel::variant))
                            .apply(instance, UnbakedClassicDeskModel::new));

    public static final Codec<UnbakedClassicDeskModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] BASIC_MODEL_PARTS_BASE = {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_base"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east_leg"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west_leg"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_east_leg"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_west_leg"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east_north"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east_south"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west_north"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west_south"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_east"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_west"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_east_corner"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_north_west_corner"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_east_corner"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_south_west_corner"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_all"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_all"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_middle_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_middle_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_right_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_right_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_left_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_left_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_mirrored_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_outer_corner_mirrored_open"),

            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_mirrored_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/classic_desk/classic_desk_cabinet_inside_corner_mirrored_open"),
    };
    private static final Identifier PARENT = Identifier.parse("block/block");
    public static final Identifier TABLE_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/desk_classic");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_classic"));
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_classic"));
                if (variant.hasStripped()) {
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_desk_classic"));
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_desk_cabinet_classic"));
                }
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_classic"));
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_classic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
            resolver.markDependency(modelPart);
        }
    }

    @Nullable
    @Override
    public BlockStateModel bake(ModelBaker loader) {
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(loader, loader.getModel(BASIC_MODEL_PARTS_BASE[19]), loader.getModel(BASIC_MODEL_PARTS_BASE[19]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(loader, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
