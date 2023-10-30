package com.unlikepaladin.pfm.blocks.models.logTable;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.*;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class UnbakedLogTableModel implements UnbakedModel {
    public static final List<String> LOG_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/log_table/template_log_table/template_log_table_middle");
            add("block/log_table/template_log_table/template_log_table_right");
            add("block/log_table/template_log_table/template_log_table_left");
            add("block/log_table/template_log_table/template_log_table");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> TABLE_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                String logType = variant.isNetherWood() ? "stem" : "log";
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/log_table/" + variant.asString() + "_" + logType+ "_table"));
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/log_table/" + variant.asString() + "_raw_" + logType+ "_table"));
                if (variant.hasStripped()) {
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/log_table/stripped_" + variant.asString() + "_" + logType+ "_table"));
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/log_table/stripped_" + variant.asString() + "_raw_" + logType+ "_table"));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/log_table/" + variant.asString() + "_natural_table"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (String part : LOG_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    if (variant.isNetherWood())
                        newPart = newPart.replace(variant.asString() + "_log", variant.asString() + "_stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
                for (String part : LOG_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString() + "_raw");
                    if (variant.isNetherWood())
                        newPart = newPart.replace(variant.asString() + "_raw_log", variant.asString() + "_raw_stem");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
                if (variant.hasStripped()) {
                    for (String part : LOG_MODEL_PARTS_BASE) {
                        String newPart = part.replace("template", "stripped_" + variant.asString() + "_raw");
                        if (variant.isNetherWood())
                            newPart = newPart.replace(variant.asString() + "_raw_log", variant.asString() + "_raw_stem");
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                    for (String part : LOG_MODEL_PARTS_BASE) {
                        String newPart = part.replace("template", "stripped_" + variant.asString());
                        if (variant.isNetherWood())
                            newPart = newPart.replace(variant.asString() + "_log", variant.asString() + "_stem");
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                for (String part : LOG_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString()).replace(variant.asString() + "_log", variant.asString() +"_natural");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

        public UnbakedLogTableModel(VariantBase variant, List<String> modelParts, BlockType type, boolean raw) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : LOG_MODEL_PARTS_BASE){
            String s = modelPartName.replace("template", variant.asString());
            if (type == BlockType.STRIPPED_LOG) {
                s = s.replace(variant.asString(), "stripped_" + variant.asString());
            }
            if (raw) {
                s = s.replace(variant.asString() + "_log", variant.asString() + "_raw_log");
                if (variant.isNetherWood())
                    s = s.replace(variant.asString()  + "_raw_log", variant.asString()  + "_raw_stem");
            }
            if (variant instanceof StoneVariant) {
                s = s.replace(variant.asString()  + "_log", variant.asString()  + "_natural");
            } else if(variant.isNetherWood()) {
                s = s.replace(variant.asString()  + "_log", variant.asString()  + "_stem");
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
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<com.mojang.datafixers.util.Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(frameTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite > textureGetter, ModelBakeSettings
    rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : MODEL_PARTS) {
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}