package com.unlikepaladin.pfm.blocks.models.kitchenCabinet;

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
public class UnbakedKitchenCabinetModel implements UnbakedModel {
    public static final List<String> CABINET_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_inner_corner_left");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_inner_corner_right");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_outer_corner_left");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_outer_corner_right");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_open");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_inner_corner_open_left");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_inner_corner_open_right");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_outer_corner_open_left");
            add("block/kitchen_cabinet/template_kitchen_cabinet/template_kitchen_cabinet_outer_corner_open_right");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> CABINET_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/" + variant.asString() + "_kitchen_cabinet"));
                if (variant.hasStripped())
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/stripped_" + variant.asString() + "_kitchen_cabinet"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                if (variant.equals(StoneVariant.QUARTZ))
                    continue;
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/" + variant.asString() + "_kitchen_cabinet"));
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/kitchen_cabinet/" + variant.asString() + "_kitchen_cabinet"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariantRegistry.getVariants()){
                for (String part : CABINET_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
                if (variant.hasStripped())
                    for (String part : CABINET_MODEL_PARTS_BASE) {
                        String newPart = part.replace("template", "stripped_" + variant.asString());
                        add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                    }
            }
            for(StoneVariant variant : StoneVariant.values()){
                if (variant.equals(StoneVariant.QUARTZ))
                    continue;
                for (String part : CABINET_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(ExtraCounterVariant variant : ExtraCounterVariant.values()){
                for (String part : CABINET_MODEL_PARTS_BASE) {
                    String newPart = part.replace("template", variant.asString());
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

    public UnbakedKitchenCabinetModel(VariantBase<?> variant, List<String> modelParts, BlockType type) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : CABINET_MODEL_PARTS_BASE){
            String s = modelPartName.replace("template", variant.asString());
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