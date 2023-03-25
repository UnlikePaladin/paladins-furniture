package com.unlikepaladin.pfm.blocks.models.logTable;

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
public class UnbakedLogTableModel implements UnbakedModel {
    public static final List<String> CLASSIC_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/log_table/log_table_middle");
            add("block/log_table/log_table_right");
            add("block/log_table/log_table_left");
            add("block/log_table/log_table");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_" + logType+ "_table"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_raw_" + logType+ "_table"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/stripped_" + variant.asString() + "_" + logType+ "_table"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/stripped_" + variant.asString() + "_raw_" + logType+ "_table"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_natural_table"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : CLASSIC_MODEL_PARTS_BASE) {
                    String newPart = part;
                    if (!variant.equals(WoodVariant.OAK))
                        newPart = part.replace("log", variant.asString() + "_log");
                    if (variant.isNetherWood())
                        newPart = newPart.replace("log", "stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : CLASSIC_MODEL_PARTS_BASE) {
                    String newPart = part;
                    if (!variant.equals(WoodVariant.OAK))
                        newPart = part.replace("log", variant.asString() + "_raw_log");
                    if (variant.isNetherWood())
                        newPart = newPart.replace("log", "stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : CLASSIC_MODEL_PARTS_BASE) {
                    String newPart = part.replace("log", "stripped_" + variant.asString() + "_raw_log");
                    if (variant.isNetherWood())
                        newPart = newPart.replace("log", "stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : CLASSIC_MODEL_PARTS_BASE) {
                    String newPart = part.replace("log", "stripped_" + variant.asString() + "_log");
                    if (variant.isNetherWood())
                        newPart = newPart.replace("log", "stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                for (String part : CLASSIC_MODEL_PARTS_BASE) {
                    String newPart = part.replace("log", variant.asString() + "_log").replace("log", "natural");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

        public UnbakedLogTableModel(MaterialEnum variant, List<String> modelParts, BlockType type, boolean raw) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : CLASSIC_MODEL_PARTS_BASE){
            String s = modelPartName;
            if (!variant.equals(WoodVariant.OAK))
                s = s.replace("log", variant.asString() + "_log");
            if (type == BlockType.STRIPPED_LOG) {
                s = s.replace(variant.asString(), "stripped_" + variant.asString());
            }
            if (raw) {
                if (variant.equals(WoodVariant.OAK)) {
                    s = s.replace("log", "oak_log");
                }
                s = s.replace("log", "raw_log");
            }
            if (variant instanceof StoneVariant) {
                s = s.replace("log", "natural");
            } else if(variant.isNetherWood()) {
                s = s.replace("log", "stem");
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
        return new BakedLogTableModel(frame, settings, bakedModels);
    }
}