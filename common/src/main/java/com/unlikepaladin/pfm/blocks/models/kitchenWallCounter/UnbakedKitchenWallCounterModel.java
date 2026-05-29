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
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedKitchenWallCounterModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenWallCounterModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedKitchenWallCounterModel::variant))
                            .apply(instance, UnbakedKitchenWallCounterModel::new));

    public static final Codec<UnbakedKitchenWallCounterModel> CODEC = MAP_CODEC.codec();

    public static final ResourceLocation[] COUNTER_MODEL_PARTS_BASE = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_inner_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_inner_corner_right"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_outer_corner_left"),
            ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_counter/kitchen_counter_middle_outer_corner_right")
    };

    public static final ResourceLocation COUNTER_MODEL_ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_wall_counter");
    public static final List<ResourceLocation> COUNTER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_counter"));
                if (variant.hasStripped())
                    add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_wall_counter"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_counter"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_wall_counter"));
            }
            add(COUNTER_MODEL_ID);
        }
    };

    public Collection<Material> getTextureDependencies(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(COUNTER_MODEL_PARTS_BASE[0]), baker.getModel(COUNTER_MODEL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(COUNTER_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(COUNTER_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(COUNTER_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(COUNTER_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : COUNTER_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(COUNTER_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(COUNTER_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation c : COUNTER_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}