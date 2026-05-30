package com.unlikepaladin.pfm.blocks.models.modernStool;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.client.model.PFMUnbakedBlockStateModel;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
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

public record UnbakedModernStoolModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedModernStoolModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedModernStoolModel::variant))
                            .apply(instance, UnbakedModernStoolModel::new));

    public static final Codec<UnbakedModernStoolModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] MODERN_STOOL_PARTS_BASE = new Identifier[] {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/modern_stool/modern_stool"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/modern_stool/modern_stool_tucked")
    };

    public static final Identifier STOOL_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/modern_stool");
    public static final List<Identifier> MODERN_STOOL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_modern_stool"));
                if (variant.hasStripped())
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_modern_stool"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_modern_stool"));
            }
            for(ExtraStoolVariant variant : ExtraStoolVariant.values()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_modern_stool"));
            }
            add(STOOL_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(MODERN_STOOL_PARTS_BASE[0]), baker.getModel(MODERN_STOOL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(STOOL_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(STOOL_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : MODERN_STOOL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(STOOL_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(STOOL_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : MODERN_STOOL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}
