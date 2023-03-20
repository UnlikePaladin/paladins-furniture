package com.unlikepaladin.pfm.blocks.models.mirror;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BakedMirrorModel implements BakedModel {
    protected final Sprite glassTex;
    protected final Sprite reflectTex;
    protected final ModelBakeSettings settings;
    private final Sprite frame;
    private final Map<Identifier,BakedModel> bakedModels;
    public BakedMirrorModel(Sprite frame, Sprite glassTex, Sprite reflectTex, ModelBakeSettings settings, Map<Identifier,BakedModel> bakedModels) {
        this.frame = frame;
        this.glassTex = glassTex;
        this.reflectTex = reflectTex;
        this.settings = settings;
        this.bakedModels = bakedModels;
    }

    public Map<Identifier,BakedModel> getBakedModels() {
        return bakedModels;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
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

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return ModelOverrideList.EMPTY;
    }
}
