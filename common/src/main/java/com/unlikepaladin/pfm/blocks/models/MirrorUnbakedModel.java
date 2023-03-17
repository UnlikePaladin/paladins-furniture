package com.unlikepaladin.pfm.blocks.models;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class MirrorUnbakedModel implements UnbakedModel {
    public static final Identifier MODEL_MIRROR_GLASS = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_base");
    public static final Identifier MODEL_MIRROR_TOP = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_top");
    public static final Identifier MODEL_MIRROR_BOTTOM = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_bottom");
    public static final Identifier MODEL_MIRROR_LEFT = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_left");
    public static final Identifier MODEL_MIRROR_RIGHT = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_right");
    public static final Identifier MODEL_MIRROR_RIGHT_TOP_CORNER = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_right_top");
    public static final Identifier MODEL_MIRROR_LEFT_TOP_CORNER = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_left_top");
    public static final Identifier MODEL_MIRROR_RIGHT_BOTTOM_CORNER = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_right_bottom");
    public static final Identifier MODEL_MIRROR_LEFT_BOTTOM_CORNER = new Identifier(PaladinFurnitureMod.MOD_ID,"block/mirror/mirror_left_bottom");

    public static final Identifier DEFAULT_FRAME_TEXTURE = new Identifier("minecraft","block/white_concrete");
    public static final Identifier DEFAULT_REFLECT = new Identifier("pfm","block/mirror");
    public static final Identifier DEFAULT_GLASS = new Identifier("minecraft","block/glass");

    private static final Identifier PARENT = new Identifier("block/block");

    private final SpriteIdentifier reflectTex;
    private final SpriteIdentifier glassTex;
    private final SpriteIdentifier frameTex;
    public MirrorUnbakedModel(Identifier reflect, Identifier defaultFrameTexture, Identifier glass) {
        this.reflectTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, reflect);
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, defaultFrameTexture);
        this.glassTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, glass);
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(PARENT);
    }
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, DEFAULT_GLASS));
        list.add(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, DEFAULT_FRAME_TEXTURE));
        list.add(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, DEFAULT_REFLECT));
        return list;
    }
    Map<Identifier,BakedModel> bakedModels = new LinkedHashMap<>();


    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        bakedModels.put(MODEL_MIRROR_GLASS, loader.getOrLoadModel(MODEL_MIRROR_GLASS).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_TOP, loader.getOrLoadModel(MODEL_MIRROR_TOP).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_BOTTOM, loader.getOrLoadModel(MODEL_MIRROR_BOTTOM).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_LEFT, loader.getOrLoadModel(MODEL_MIRROR_LEFT).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_RIGHT, loader.getOrLoadModel(MODEL_MIRROR_RIGHT).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_RIGHT_TOP_CORNER, loader.getOrLoadModel(MODEL_MIRROR_RIGHT_TOP_CORNER).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_LEFT_TOP_CORNER, loader.getOrLoadModel(MODEL_MIRROR_LEFT_TOP_CORNER).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_RIGHT_BOTTOM_CORNER, loader.getOrLoadModel(MODEL_MIRROR_RIGHT_BOTTOM_CORNER).bake(loader, textureGetter, rotationContainer, modelId));
        bakedModels.put(MODEL_MIRROR_LEFT_BOTTOM_CORNER, loader.getOrLoadModel(MODEL_MIRROR_LEFT_BOTTOM_CORNER).bake(loader, textureGetter, rotationContainer, modelId));
        return getBakedModel(textureGetter.apply(frameTex), null, textureGetter.apply(glassTex), textureGetter.apply(reflectTex), rotationContainer, bakedModels);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier,BakedModel> bakedModels) {
        return new BakedMirrorModel(frame, frameOverrides, glassTex, reflectTex, settings, bakedModels);
    }
}
