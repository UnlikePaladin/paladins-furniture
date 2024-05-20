package com.unlikepaladin.pfm.blocks.models.basicTable;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
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
public class UnbakedBasicTableModel implements UnbakedModel {
    public static final Identifier[] BASIC_MODEL_PARTS_BASE = {
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_base"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_top"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_top"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_north"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_east_west_south"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east_bottom"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west_bottom"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_east"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_south_west"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_east_corner"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_north_west_corner"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_east_corner"),
            new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic/table_basic_south_west_corner")
    };
    private static final Identifier PARENT = new Identifier("block/block");
    public static final Identifier TABLE_MODEL_ID = new Identifier(PaladinFurnitureMod.MOD_ID, "block/table_basic");
    public static final List<Identifier> MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_table_basic"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/stripped_" + variant.asString() + "_table_basic"));
            }
            for(StoneVariant variant : StoneVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "item/" + variant.asString() + "_table_basic"));
            }
            add(TABLE_MODEL_ID);
        }
    };

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
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
        for (Identifier modelPart : BASIC_MODEL_PARTS_BASE) {
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
