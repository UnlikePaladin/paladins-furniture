package com.unlikepaladin.pfm.blocks.models.mirror;

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

@Environment(EnvType.CLIENT)
public class UnbakedMirrorModel implements UnbakedModel {
    public static final String[] MODEL_PARTS = new String[] {"block/mirror/mirror_base", "block/mirror/mirror_top", "block/mirror/mirror_bottom", "block/mirror/mirror_left","block/mirror/mirror_right", "block/mirror/mirror_right_top", "block/mirror/mirror_left_top", "block/mirror/mirror_right_bottom", "block/mirror/mirror_left_bottom"};
    public static final Identifier DEFAULT_FRAME_TEXTURE = new Identifier("minecraft","block/white_concrete");
    public static final Identifier DEFAULT_REFLECT = new Identifier("pfm","block/mirror");
    public static final Identifier DEFAULT_GLASS = new Identifier("minecraft","block/glass");

    private static final Identifier PARENT = new Identifier("block/block");
    public static final Identifier[] MIRROR_MODEL_IDS = {new Identifier(PaladinFurnitureMod.MOD_ID, "block/white_mirror")};


    protected final SpriteIdentifier reflectTex;
    protected final SpriteIdentifier glassTex;
    protected final SpriteIdentifier frameTex;
    public UnbakedMirrorModel(Identifier reflect, Identifier defaultFrameTexture, Identifier glass) {
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

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPart: MODEL_PARTS) {
            bakedModels.put(modelPart, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPart), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), textureGetter.apply(glassTex), textureGetter.apply(reflectTex), rotationContainer, bakedModels);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<String,BakedModel> bakedModels) {
        return new BakedMirrorModel(frame, glassTex, reflectTex, settings, bakedModels);
    }
}
