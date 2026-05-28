package com.unlikepaladin.pfm.blocks.models.herringbone;

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
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record UnbakedHerringboneModel(Variant variant) implements PFMUnbakedBlockStateModel {

    public static final MapCodec<UnbakedHerringboneModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedHerringboneModel::variant))
                            .apply(instance, UnbakedHerringboneModel::new));

    public static final Codec<UnbakedHerringboneModel> CODEC = MAP_CODEC.codec();

    private static final List<ResourceLocation> TEMPLATE_MODEL = List.of(ResourceLocation.parse("minecraft:block/block"), ResourceLocation.parse("minecraft:block/cube_all"));

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/herringbone_planks");
    public static final List<ResourceLocation> MODEL_IDS = new ArrayList<>() {
        {
            add(ID);
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_herringbone_planks"));
                add(ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getSerializedName() + "_herringbone_planks"));
            }
        }
    };

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation modelPart : TEMPLATE_MODEL) {
            resolver.markDependency(modelPart);
        }
    }

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ResolvedModel model = baker.getModel(TEMPLATE_MODEL.getFirst());
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, model, model.getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(ID) && PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(ID))
            PFMRuntimeResources.modelCacheMap.put(ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (ResourceLocation modelPart : TEMPLATE_MODEL) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(ResourceLocation modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
