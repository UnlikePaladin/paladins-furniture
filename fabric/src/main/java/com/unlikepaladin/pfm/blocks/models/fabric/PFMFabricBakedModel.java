package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.sprite.FabricMaterialBaker;
import net.fabricmc.fabric.api.client.renderer.v1.sprite.FabricTextureAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.sprite.SpriteFinder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBlockStateModel, PFMBakedModelParticleExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    public PFMFabricBakedModel(ModelState settings, ModelRenderProperties itemBakeSettings, List<BlockStateModelPart> bakedModels) {
        super(settings, itemBakeSettings, bakedModels);
    }

    public void pushTextureTransform(QuadEmitter context, TextureAtlasSprite sprite) {
        context.pushTransform(quad -> {
            AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
            FabricTextureAtlas textureAtlas = atlasManager.getAtlasOrThrow(AtlasIds.BLOCKS);
            TextureAtlasSprite originalSprite = textureAtlas.spriteFinder().find(quad);
            if (originalSprite.contents().name() != sprite.contents().name()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = ModelHelper.getFrameFromU(originalSprite, quad.u(index));
                    float frameV = ModelHelper.getFrameFromV(originalSprite, quad.v(index));
                    quad.uv(index, sprite.getU(frameU), sprite.getV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, AtlasIds.BLOCKS);
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement, Identifier atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
                FabricTextureAtlas textureAtlas = atlasManager.getAtlasOrThrow(AtlasIds.BLOCKS);
                TextureAtlasSprite originalSprite = textureAtlas.spriteFinder().find(quad);
                Identifier keyId = originalSprite.contents().name();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).contents().name()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    TextureAtlasSprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = ModelHelper.getFrameFromU(originalSprite, quad.u(index));
                        float frameV = ModelHelper.getFrameFromV(originalSprite, quad.v(index));
                        quad.uv(index, sprite.getU(frameU), sprite.getV(frameV));
                    }
                }
            }
            return true;
        });
    }


    @Override
    public TextureAtlasSprite pfm$getParticle(Level world, BlockPos pos, BlockState state) {
        return pfm$getParticle(state);
    }

    @Override
    public Material.Baked particleMaterial() {
        return getTemplateBakedModels().get(0).particleMaterial();
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        return new Material.Baked(pfm$getParticle(state), false);
    }
    @Override
    public void setBlockStateProperty(BlockState state) {
        this.blockState = state;
    }

    @Override
    public void setVariant(VariantBase<?> variant) {
        this.variant = variant;
    }

    @Override
    public BlockState getBlockStateProperty() {
        return blockState;
    }

    @Override
    public VariantBase<?> getVariant() {
        return variant;
    }


    public void emitModelQuads(QuadEmitter emitter, BlockStateModel model, RandomSource random) {
        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(random, parts);
        int partCount = parts.size();
        for(int i = 0; i < partCount; ++i) {
            parts.get(i).emitQuads(emitter, null);
        }
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return getTemplateBakedModels().getFirst().materialFlags();
    }

    abstract public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier);
}
