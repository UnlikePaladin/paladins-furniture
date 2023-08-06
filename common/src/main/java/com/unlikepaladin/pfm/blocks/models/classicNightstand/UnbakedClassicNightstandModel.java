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
import java.util.function.Function;

public class UnbakedClassicNightstandModel implements UnbakedModel {
    public static final List<String> NIGHTSTAND_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_middle");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_right");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_left");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_middle_open");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_right_open");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_left_open");
            add("block/classic_nightstand/template_classic_nightstand/template_classic_nightstand_open");

        }
    };
    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

    public UnbakedClassicNightstandModel(VariantBase<?> variant, List<String> modelParts, BlockType type) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        
        for(String modelPartName : NIGHTSTAND_MODEL_PARTS_BASE){
            String s = modelPartName.replace("template", variant.asString());
            if (type == BlockType.STRIPPED_LOG) {
                s = s.replace(variant.asString(), "stripped_" + variant.asString());
            }
            modelParts.add(s);
        }
        this.MODEL_PARTS = modelParts;
    }

    public static final List<Identifier> NIGHSTAND_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/" + variant.asString() + "_classic_nightstand"));
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/stripped_" + variant.asString() + "_classic_nightstand"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/classic_nightstand/" + variant.asString() + "_classic_nightstand"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                for (String part : NIGHTSTAND_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                
                for (String part : NIGHTSTAND_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", "stripped_" + variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                
                for (String part : NIGHTSTAND_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
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
        List<SpriteIdentifier> list = new ArrayList<>(1);
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
