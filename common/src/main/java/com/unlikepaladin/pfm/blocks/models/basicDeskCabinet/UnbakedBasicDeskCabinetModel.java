package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.StoneVariant;
import com.unlikepaladin.pfm.data.materials.StoneVariantRegistry;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.runtime.PFMBakedModelContainer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedBasicDeskCabinetModel implements UnbakedModel {
    public static final Identifier[] BASIC_MODEL_PARTS_BASE = new Identifier[] {
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_closed"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_left_closed"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_right_closed"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_middle_closed"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_closed_left"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_closed_right"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_closed_left"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_closed_right"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_left_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_right_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_middle_open"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_open_left"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_inside_corner_open_right"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_open_left"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_outer_corner_open_right"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_north_east"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_north_west"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_south_east"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/basic_desk_cabinet/basic_desk_cabinet_leg_south_west")
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final Identifier TABLE_MODEL_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "block/desk_cabinet_basic");
    public static final List<Identifier> MODEL_IDS = new ArrayList<Identifier>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_desk_cabinet_basic"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_desk_cabinet_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_desk_cabinet_basic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
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
        if (PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID) && PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().containsKey(rotationContainer))
            return getBakedModel(TABLE_MODEL_ID, rotationContainer, PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().get(rotationContainer));

        if (!PFMRuntimeResources.modelCacheMap.containsKey(TABLE_MODEL_ID))
            PFMRuntimeResources.modelCacheMap.put(TABLE_MODEL_ID, new PFMBakedModelContainer());

        List<BakedModel> bakedModelList = new ArrayList<>();
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
            bakedModelList.add(loader.bake(modelPart, rotationContainer));
        }

        PFMRuntimeResources.modelCacheMap.get(TABLE_MODEL_ID).getCachedModelParts().put(rotationContainer, bakedModelList);
        return getBakedModel(TABLE_MODEL_ID, rotationContainer, bakedModelList);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Identifier modelId, ModelBakeSettings settings, List<BakedModel> modelParts) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}