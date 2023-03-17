package com.unlikepaladin.pfm.blocks.models;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BakedMirrorModel implements BakedModel {

    private final Map<BlockState, Sprite> frameOverrides;

    protected final Sprite glassTex;
    protected final Sprite reflectTex;
    protected final ModelBakeSettings settings;
    private final Sprite frame;
    private final List<BakedModel> bakedModels;
    public BakedMirrorModel(Sprite frame, Map<BlockState, Sprite> frameOverrides, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, List<BakedModel> bakedModels) {
        this.frame = frame;
        this.frameOverrides = frameOverrides;
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
        this.settings = settings;
        this.bakedModels = bakedModels;
    }
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        List<BakedQuad> quads = new ArrayList<>();
        bakedModels.forEach(bakedModel -> {
            quads.addAll(bakedModel.getQuads(state, face, random));
        });
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean hasDepth() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return frame;
    }
    private static Transformation makeTransform(float rotationX, float rotationY, float rotationZ, float translationX, float translationY, float translationZ, float scaleX, float scaleY, float scaleZ) {
        Vec3f translation = new Vec3f(translationX, translationY, translationZ);
        translation.scale(0.0625f);
        translation.clamp(-5.0F, 5.0F);
        return new Transformation(new Vec3f(rotationX, rotationY, rotationZ), translation, new Vec3f(scaleX, scaleY, scaleZ));
    }

    public static final Transformation TRANSFORM_BLOCK_GUI = makeTransform(30, 225, 0, 0, 0, 0, 0.625f, 0.625f, 0.625f);
    public static final Transformation TRANSFORM_BLOCK_GROUND = makeTransform(0, 0, 0, 0, 3, 0, 0.25f, 0.25f, 0.25f);
    public static final Transformation TRANSFORM_BLOCK_FIXED = makeTransform(0, 0, 0, 0, 0, 0, 0.5f, 0.5f, 0.5f);
    public static final Transformation TRANSFORM_BLOCK_3RD_PERSON_RIGHT = makeTransform(75, 45, 0, 0, 2.5f, 0, 0.375f, 0.375f, 0.375f);
    public static final Transformation TRANSFORM_BLOCK_1ST_PERSON_RIGHT = makeTransform(0, 45, 0, 0, 0, 0, 0.4f, 0.4f, 0.4f);
    public static final Transformation TRANSFORM_BLOCK_1ST_PERSON_LEFT = makeTransform(0, 225, 0, 0, 0, 0, 0.4f, 0.4f, 0.4f);

    public static final ModelTransformation MODEL_TRANSFORM_BLOCK = new ModelTransformation(TRANSFORM_BLOCK_3RD_PERSON_RIGHT, TRANSFORM_BLOCK_3RD_PERSON_RIGHT, TRANSFORM_BLOCK_1ST_PERSON_LEFT, TRANSFORM_BLOCK_1ST_PERSON_RIGHT, Transformation.IDENTITY, TRANSFORM_BLOCK_GUI, TRANSFORM_BLOCK_GROUND, TRANSFORM_BLOCK_FIXED);


    @Override
    public ModelTransformation getTransformation() {
        return MODEL_TRANSFORM_BLOCK;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
}
