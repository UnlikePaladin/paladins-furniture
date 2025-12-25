package com.unlikepaladin.pfm.blocks.models.herringbone;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class UnbakedHerringboneModel implements UnbakedModel {

    private static final List<Identifier> TEMPLATE_MODEL = List.of(Identifier.of("minecraft:block/cube_all"));
    private final Identifier id;
    public UnbakedHerringboneModel(Identifier id) {
        this.id = id;
    }

    public static final Identifier ID = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/herringbone_planks");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            add(ID);
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_herringbone_planks"));
                add(Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_herringbone_planks"));
            }
        }
    };

    @Override
    public void resolve(Resolver resolver) {
        for (Identifier modelPart : TEMPLATE_MODEL) {
            resolver.resolve(modelPart);
        }
    }

    @Override
    public @Nullable BakedModel bake(ModelTextures textures, Baker loader, ModelBakeSettings rotationContainer, boolean ambientOcclusion, boolean isSideLit, ModelTransformation transformation) {
        if (PFMRuntimeResources.modelCacheMap.containsKey(id) && PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(id, rotationContainer, PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(id))
            PFMRuntimeResources.modelCacheMap.put(id, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : TEMPLATE_MODEL) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(id).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(id, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Identifier modelId, ModelBakeSettings settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
