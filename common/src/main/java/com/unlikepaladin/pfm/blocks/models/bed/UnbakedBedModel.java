package com.unlikepaladin.pfm.blocks.models.bed;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
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
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;


@Environment(EnvType.CLIENT)
public class UnbakedBedModel implements UnbakedModel {
    public static final List<String> SIMPLE_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/simple_bed/mattresses/red_foot_mattress");
            add("block/simple_bed/mattresses/red_head_mattress");
            add("block/simple_bed/oak/foot/simple_bed_foot");
            add("block/simple_bed/oak/head/simple_bed_head");
            add("block/simple_bed/oak/foot/simple_bed_foot_right");
            add("block/simple_bed/oak/foot/simple_bed_foot_left");
            add("block/simple_bed/oak/head/simple_bed_head_right");
            add("block/simple_bed/oak/head/simple_bed_head_left");
            add("block/simple_bed/oak/bunk/foot/simple_bed_foot_right");
            add("block/simple_bed/oak/bunk/foot/simple_bed_foot_left");
            add("block/simple_bed/oak/bunk/head/simple_bed_head");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> BED_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_" + dyeColor.getName() + "_simple_bed"));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_" + dyeColor.getName() + "_classic_bed"));
                }
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    for (String part : SIMPLE_MODEL_PARTS_BASE) {
                        String newPart = part.replace("oak", variant.asString()).replace("red", dyeColor.getName());
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    for (String part : SIMPLE_MODEL_PARTS_BASE) {
                        if (part.contains("bunk/head")) {
                            continue;
                        }
                        String newPart = part.replace("oak", variant.asString()).replace("red", dyeColor.getName()).replace("simple", "classic");
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                }
            }
        }
    };

    protected final SpriteIdentifier beddingTex;
    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;
    private final boolean isClassic;
    public UnbakedBedModel(Identifier defaultFrameTexture, Identifier beddingTexture, WoodVariant variant, DyeColor color, List<String> modelParts, boolean isClassic) {
        this.beddingTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, beddingTexture);
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, defaultFrameTexture);
        for(String modelPartName : SIMPLE_MODEL_PARTS_BASE){
            String s = modelPartName.replace("oak", variant.asString()).replace("red", color.getName());
            if (isClassic) {
                s = s.replace("simple", "classic");
            }
            modelParts.add(s);
        }
        this.isClassic = isClassic;
        this.MODEL_PARTS = modelParts;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(frameTex);
        list.add(beddingTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : MODEL_PARTS) {
            if (isClassic && modelPart.contains("bunk/head")) {
                continue;
            }
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), textureGetter.apply(beddingTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, Sprite beddingTex, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        return new BakedBedModel(frame, beddingTex, settings, bakedModels);
    }
}