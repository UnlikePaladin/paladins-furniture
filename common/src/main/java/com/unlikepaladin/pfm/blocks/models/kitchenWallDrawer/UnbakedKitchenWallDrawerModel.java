package com.unlikepaladin.pfm.blocks.models.kitchenWallDrawer;

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
public record UnbakedKitchenWallDrawerModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenWallDrawerModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedKitchenWallDrawerModel::variant))
                            .apply(instance, UnbakedKitchenWallDrawerModel::new));

    public static final Codec<UnbakedKitchenWallDrawerModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] COUNTER_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_open"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_inner_corner_right"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_open_left"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_drawer/kitchen_drawer_middle_outer_corner_open_right")
    };


    public static final Identifier DRAWER_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_wall_drawer");
    public static final List<Identifier> DRAWER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
                if (variant.hasStripped())
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_drawer"));
            }
            add(DRAWER_MODEL_ID);
        }
    };

    public Collection<Material> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(COUNTER_MODEL_PARTS_BASE[0]), baker.getModel(COUNTER_MODEL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(DRAWER_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(DRAWER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(DRAWER_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : COUNTER_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(DRAWER_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(DRAWER_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : COUNTER_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}