package com.unlikepaladin.pfm.blocks.models.kitchenDrawer;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.materials.*;
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
public class UnbakedKitchenDrawerModel implements UnbakedModel {
    public static final List<String> COUNTER_MODEL_PARTS_BASE = new ArrayList<>() {
        {
            add("block/kitchen_drawer/kitchen_drawer");
            add("block/kitchen_drawer/kitchen_drawer_edge_left");
            add("block/kitchen_drawer/kitchen_drawer_edge_right");
            add("block/kitchen_drawer/kitchen_drawer_inner_corner_left");
            add("block/kitchen_drawer/kitchen_drawer_inner_corner_right");
            add("block/kitchen_drawer/kitchen_drawer_outer_corner_left");
            add("block/kitchen_drawer/kitchen_drawer_outer_corner_right");
            add("block/kitchen_drawer/kitchen_drawer_open");
            add("block/kitchen_drawer/kitchen_drawer_edge_left_open");
            add("block/kitchen_drawer/kitchen_drawer_edge_right_open");
            add("block/kitchen_drawer/kitchen_drawer_inner_corner_left");
            add("block/kitchen_drawer/kitchen_drawer_inner_corner_right");
            add("block/kitchen_drawer/kitchen_drawer_outer_corner_open_left");
            add("block/kitchen_drawer/kitchen_drawer_outer_corner_open_right");
        }
    };

    private static final Identifier PARENT = new Identifier("block/block");
    public static final List<Identifier> DRAWER_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_kitchen_drawer"));
            }
            for(WoodVariant variant : WoodVariant.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/stripped_" + variant.asString() + "_kitchen_drawer"));
            }
            for(StoneVariant variant : StoneVariant.values()){
                if (variant.equals(StoneVariant.QUARTZ))
                    continue;
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_kitchen_drawer"));
            }
            for(ExtraCounterVariants variant : ExtraCounterVariants.values()){
                add(new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.asString() + "_kitchen_drawer"));
            }
        }
    };

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<>() {
        {
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : COUNTER_MODEL_PARTS_BASE) {
                    String newPart = part;
                    if (!variant.equals(WoodVariant.OAK))
                        newPart = part.replace("kitchen", variant.asString() + "_kitchen");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(WoodVariant variant : WoodVariant.values()){
                for (String part : COUNTER_MODEL_PARTS_BASE) {
                    String newPart = part.replace("kitchen", "stripped_" + variant.asString() + "_kitchen");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(StoneVariant variant : StoneVariant.values()){
                if (variant.equals(StoneVariant.QUARTZ))
                    continue;
                for (String part : COUNTER_MODEL_PARTS_BASE) {
                    String newPart = part.replace("kitchen", variant.asString() + "_kitchen");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
            for(ExtraCounterVariants variant : ExtraCounterVariants.values()){
                for (String part : COUNTER_MODEL_PARTS_BASE) {
                    String newPart = part.replace("kitchen", variant.asString() + "_kitchen");
                    add(new Identifier(PaladinFurnitureMod.MOD_ID, newPart));
                }
            }
        }
    };

    protected final SpriteIdentifier frameTex;
    private final List<String> MODEL_PARTS;

        public UnbakedKitchenDrawerModel(MaterialEnum variant, List<String> modelParts, BlockType type) {
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(type));
        for(String modelPartName : COUNTER_MODEL_PARTS_BASE){
            String s = modelPartName;
            if (!variant.equals(WoodVariant.OAK) || !type.equals(BlockType.PLANKS)) {
                s = s.replace("kitchen", variant.asString() + "_kitchen");
            }
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
        return new BakedKitchenDrawerModel(frame, settings, bakedModels);
    }
}