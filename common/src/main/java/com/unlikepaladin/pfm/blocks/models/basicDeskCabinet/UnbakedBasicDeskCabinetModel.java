package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet;

import com.mojang.datafixers.util.Pair;
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
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedBasicDeskCabinetModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedBasicDeskCabinetModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedBasicDeskCabinetModel::variant))
                            .apply(instance, UnbakedBasicDeskCabinetModel::new));

    public static final Codec<UnbakedBasicDeskCabinetModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] BASIC_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_left_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_right_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_middle_closed"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_closed_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_closed_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_closed_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_closed_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_open"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_left_open"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_right_open"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_middle_open"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_open_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_open_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_open_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_open_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_north_east"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_north_west"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_south_east"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_south_west")
    };

    private static final Identifier PARENT = Identifier.parse("block/block");
    public static final Identifier TABLE_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/desk_cabinet_basic");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_basic"));
                if (variant.hasStripped())
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_desk_cabinet_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_desk_cabinet_basic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : BASIC_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    public Collection<Material> getMaterials(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(BASIC_MODEL_PARTS_BASE[0]), baker.getModel(BASIC_MODEL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(TABLE_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
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