package com.unlikepaladin.pfm.blocks.models.classicNightstand;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UnbakedClassicNightstandModel implements UnbakedModel {
    public static final Identifier[] NIGHTSTAND_MODEL_PARTS_BASE = new Identifier[] {
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_middle_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_right_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_left_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/classic_nightstand_open")
    };

    public static final Identifier NIGHTSTAND_MODEL_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand");
    public static final List<Identifier> NIGHSTAND_MODEL_IDS = new ArrayList<Identifier>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_classic_nightstand"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
            }
            add(NIGHTSTAND_MODEL_ID);
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public Collection<Identifier> getModelDependencies() {
        return Collections.singleton(PARENT);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(NIGHTSTAND_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(NIGHTSTAND_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(NIGHTSTAND_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : NIGHTSTAND_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(NIGHTSTAND_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(NIGHTSTAND_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Identifier modelId, ModelBakeSettings settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
