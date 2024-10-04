package com.unlikepaladin.pfm.blocks.models.kitchenSink;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedKitchenSinkModel implements UnbakedModel {
    public static final Identifier[] SINK_MODEL_PARTS_BASE = new Identifier[] {
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_level1"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_level2"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink/kitchen_sink_full"),
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final Identifier SINK_MODEL_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_sink");
    public static final List<Identifier> SINK_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_sink"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_kitchen_sink"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                if (variant.identifier.getPath().equals("quartz"))
                    continue;
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_sink"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_kitchen_sink"));
            }
            add(SINK_MODEL_ID);
        }
    };

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(SINK_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(SINK_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(SINK_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(SINK_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : SINK_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(SINK_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(SINK_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Identifier modelId, ModelBakeSettings settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}