package com.unlikepaladin.pfm.blocks.models.bed;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.*;
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
            add("block/simple_bed/mattresses/color_foot_mattress");
            add("block/simple_bed/mattresses/color_head_mattress");
            add("block/simple_bed/template/foot/simple_bed_foot");
            add("block/simple_bed/template/head/simple_bed_head");
            add("block/simple_bed/template/foot/simple_bed_foot_right");
            add("block/simple_bed/template/foot/simple_bed_foot_left");
            add("block/simple_bed/template/head/simple_bed_head_right");
            add("block/simple_bed/template/head/simple_bed_head_left");
            add("block/simple_bed/template/bunk/foot/simple_bed_foot_right");
            add("block/simple_bed/template/bunk/foot/simple_bed_foot_left");
            add("block/simple_bed/template/bunk/head/simple_bed_head");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> BED_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_" + dyeColor.getName() + "_simple_bed"));
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_" + dyeColor.getName() + "_classic_bed"));
                }
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    for (String part : SIMPLE_MODEL_PARTS_BASE) {
                        String newPart = part.replaceAll("template", variant.asString()).replace("color", dyeColor.getName());
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                }
            }
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (DyeColor dyeColor : DyeColor.values()) {
                    for (String part : SIMPLE_MODEL_PARTS_BASE) {
                        if (part.contains("bunk/head")) {
                            continue;
                        }
                        String newPart = part.replaceAll("template", variant.asString()).replace("color", dyeColor.getName()).replaceAll("simple", "classic");
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;
    private final boolean isClassic;
    public UnbakedBedModel(WoodVariant variant, DyeColor color, List<String> modelParts, boolean isClassic) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PLANKS));
        for(String modelPartName : SIMPLE_MODEL_PARTS_BASE){
            
            String s = modelPartName.replaceAll("template", variant.asString()).replace("color", color.getName());
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
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(frameTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(Baker loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart : MODEL_PARTS) {
            if (isClassic && modelPart.contains("bunk/head")) {
                continue;
            }
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}