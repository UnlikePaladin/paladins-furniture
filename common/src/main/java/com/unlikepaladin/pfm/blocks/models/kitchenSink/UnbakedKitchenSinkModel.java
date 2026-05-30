package com.unlikepaladin.pfm.blocks.models.kitchenSink;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.kitchenDrawer.UnbakedKitchenDrawerModel;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public record UnbakedKitchenSinkModel(Variant variant) implements PFMUnbakedBlockStateModel {
    public static final MapCodec<UnbakedKitchenSinkModel> MAP_CODEC = RecordCodecBuilder.mapCodec
            (instance ->
                    instance.group(Variant.MAP_CODEC.forGetter(UnbakedKitchenSinkModel::variant))
                            .apply(instance, UnbakedKitchenSinkModel::new));

    public static final Codec<UnbakedKitchenSinkModel> CODEC = MAP_CODEC.codec();

    public static final Identifier[] SINK_MODEL_PARTS_BASE = new Identifier[] {
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_level1"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_level2"),
            Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_full"),
    };

    private static final Identifier PARENT = Identifier.parse("block/block");
    public static final Identifier SINK_MODEL_ID = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink");
    public static final List<Identifier> SINK_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_sink"));
                if (variant.hasStripped())
                    add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.getSerializedName() + "_kitchen_sink"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_sink"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "item/" + variant.getSerializedName() + "_kitchen_sink"));
            }
            add(SINK_MODEL_ID);
        }
    };

    @Override
    public BlockStateModel bake(ModelBaker baker){
        ModelState settings = variant.modelState().asModelState();
        ModelRenderProperties itemSettings = ModelRenderProperties.fromResolvedModel(baker, baker.getModel(SINK_MODEL_PARTS_BASE[0]), baker.getModel(SINK_MODEL_PARTS_BASE[0]).getTopTextureSlots());

        if (PFMRuntimeResources.modelCacheMap.containsKey(SINK_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().containsKey(settings))
            return getBakedModel(SINK_MODEL_ID, settings, itemSettings, PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().get(settings));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(SINK_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(SINK_MODEL_ID, new PFMBakedModelContainer());

        List<BlockModelPart> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : SINK_MODEL_PARTS_BASE) {
            bakedModelList.add(SimpleModelWrapper.bake(baker, modelPart, settings));
        }

        PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().put(settings, bakedModelList);
        return getBakedModel(SINK_MODEL_ID, settings, itemSettings, bakedModelList);
    }

    @ExpectPlatform
    public static BlockStateModel getBakedModel(Identifier modelId, ModelState settings, ModelRenderProperties itemSettings, List<BlockModelPart> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (Identifier c : SINK_MODEL_PARTS_BASE)
            resolver.markDependency(c);
    }

    @Override
    public MapCodec<? extends BlockStateModel.Unbaked> getCodec() {
        return MAP_CODEC;
    }
}