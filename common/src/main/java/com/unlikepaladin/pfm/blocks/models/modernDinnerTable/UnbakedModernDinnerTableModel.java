package com.unlikepaladin.pfm.blocks.models.modernDinnerTable;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.materials.BlockType;
import com.unlikepaladin.pfm.blocks.materials.MaterialEnum;
import com.unlikepaladin.pfm.blocks.materials.StoneVariant;
import com.unlikepaladin.pfm.blocks.materials.WoodVariant;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedModernDinnerTableModel implements UnbakedModel {
    public static final List<String> MODERN_DINNER_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/table_modern_dinner/table_modern_dinner_middle");
            add("block/table_modern_dinner/table_modern_dinner_right");
            add("block/table_modern_dinner/table_modern_dinner_left");
            add("block/table_modern_dinner/table_modern_dinner");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_table_modern_dinner"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/stripped_" + variant.asString() + "_table_modern_dinner"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_table_modern_dinner"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : MODERN_DINNER_MODEL_PARTS_BASE) {
                    String newPart = part;
                    if (!variant.equals(WoodVariant.OAK))
                        newPart = part.replace("table", variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : MODERN_DINNER_MODEL_PARTS_BASE) {
                    String newPart = part.replace("table", "stripped_" + variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                for (String part : MODERN_DINNER_MODEL_PARTS_BASE) {
                    String newPart = part.replace("table", variant.asString() + "_table");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

        public UnbakedModernDinnerTableModel(MaterialEnum variant, List<String> modelParts, BlockType type) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : MODERN_DINNER_MODEL_PARTS_BASE){
            String s = modelPartName;
            if (!variant.equals(WoodVariant.OAK) || !type.equals(BlockType.PLANKS))
                s = s.replace("table", variant.asString() + "_table");
            if (type == BlockType.STRIPPED_LOG) {
                s = s.replace(variant.asString(), "stripped_" + variant.asString());
            }
            modelParts.add(s);
        }
        this.MODEL_PARTS = modelParts;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(frameTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite > textureGetter, ModelBakeSettings
    rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : MODEL_PARTS) {
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new BakedModernDinnerTableModel(frame, settings, bakedModels);
    }
}