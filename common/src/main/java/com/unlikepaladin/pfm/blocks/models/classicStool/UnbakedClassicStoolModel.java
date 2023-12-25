package com.unlikepaladin.pfm.blocks.models.classicStool;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.ExtraStoolVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
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

public class UnbakedClassicStoolModel implements UnbakedModel {
    public static final Identifier[] CLASSIC_STOOL_PARTS_BASE = new Identifier[] {
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_stool/classic_stool"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_stool/classic_stool_tucked")
    };

    public static final Identifier STOOL_MODEL_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_stool");
    public static final List<Identifier> CLASSIC_STOOL_MODEL_IDS = new ArrayList<Identifier>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_stool"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_classic_stool"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_classic_stool"));
            }
            add(STOOL_MODEL_ID);
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

    public static final Map<ModelBakeSettings, List<BakedModel>> CACHED_MODELS = new ConcurrentHashMap<>();
    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if (CACHED_MODELS.containsKey(rotationContainer))
            return getBakedModel(rotationContainer, CACHED_MODELS.get(rotationContainer));

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : CLASSIC_STOOL_PARTS_BASE) {
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
