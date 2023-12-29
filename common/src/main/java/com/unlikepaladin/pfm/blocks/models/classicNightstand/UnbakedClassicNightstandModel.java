package com.unlikepaladin.pfm.blocks.models.classicNightstand;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
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
    public static final List<Identifier> NIGHSTAND_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_classic_nightstand"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_nightstand"));
            }
            add(NIGHTSTAND_MODEL_ID);
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    public static final Map<ModelBakeSettings, List<BakedModel>> CACHED_MODELS = new ConcurrentHashMap<>();
    @Nullable
    @Override
    public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if (CACHED_MODELS.containsKey(rotationContainer))
            return getBakedModel(rotationContainer, CACHED_MODELS.get(rotationContainer));

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : NIGHTSTAND_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }
        CACHED_MODELS.put(rotationContainer, bakedModelList);
        return getBakedModel(rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
