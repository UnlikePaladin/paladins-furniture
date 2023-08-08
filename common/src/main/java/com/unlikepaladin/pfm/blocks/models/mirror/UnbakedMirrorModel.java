package com.unlikepaladin.pfm.blocks.models.mirror;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
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
public class UnbakedMirrorModel implements UnbakedModel {
    public static final String[] BASE_MODEL_PARTS = new String[] {"block/mirror/mirror_base", "block/mirror/mirror_top", "block/mirror/mirror_bottom", "block/mirror/mirror_left","block/mirror/mirror_right", "block/mirror/mirror_right_top", "block/mirror/mirror_left_top", "block/mirror/mirror_right_bottom", "block/mirror/mirror_left_bottom"};
    public static final Identifier[] DEFAULT_TEXTURES = new Identifier[] {new Identifier("minecraft","block/white_concrete"), new Identifier("minecraft","block/glass"), new Identifier("pfm","block/mirror")};
    private static final Identifier PARENT = new Identifier("block/block");
    public static final Identifier[] MIRROR_MODEL_IDS = {new Identifier(PaladinFurnitureMod.MOD_ID, "block/white_mirror"), new Identifier(PaladinFurnitureMod.MOD_ID, "block/gray_mirror")};
    private final List<String> MODEL_PARTS;
    protected final SpriteIdentifier reflectTex;
    protected final SpriteIdentifier glassTex;
    protected final SpriteIdentifier frameTex;
    public UnbakedMirrorModel(Identifier reflect, Identifier defaultFrameTexture, Identifier glass, List<String> modelParts, DyeColor color) {
        this.reflectTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, reflect);
        this.frameTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, defaultFrameTexture);
        this.glassTex = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, glass);
        for(String modelPartName : BASE_MODEL_PARTS){
            String s = modelPartName;
            if (color != DyeColor.WHITE)
                s = s.replace("mirror", color.getName()+"_mirror");
            modelParts.add(s);
        }
        MODEL_PARTS = modelParts;
    }

    public static final List<Identifier> ALL_MODEL_IDS = new ArrayList<Identifier>() {
        {
            for (String part : BASE_MODEL_PARTS) {
                add(new Identifier(PaladinFurnitureMod.MOD_ID, part));
            }
            for (String part : BASE_MODEL_PARTS) {
                part = part.replace("mirror", "gray_mirror");
                add(new Identifier(PaladinFurnitureMod.MOD_ID, part));
            }
        }
    };

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.singleton(PARENT);
    }
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        List<SpriteIdentifier> list = new ArrayList<>(2);
        list.add(glassTex);
        list.add(frameTex);
        list.add(reflectTex);
        return list;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        Map<String,BakedModel> bakedModels = new LinkedHashMap<>();
        for (String modelPartName: MODEL_PARTS) {
            bakedModels.put(modelPartName, loader.bake(new Identifier(PaladinFurnitureMod.MOD_ID, modelPartName), rotationContainer));
        }
        return getBakedModel(textureGetter.apply(frameTex), textureGetter.apply(glassTex), textureGetter.apply(reflectTex), rotationContainer, bakedModels, MODEL_PARTS);
    }

    @ExpectPlatform
    public static BakedModel getBakedModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<String,BakedModel> bakedModels, List<String> MODEL_PARTS) {
        throw new RuntimeException("Method wasn't replaced correctly");
    }
}
