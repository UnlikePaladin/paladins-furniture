package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBlockStateModel, PFMBakedModelParticleExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    public PFMFabricBakedModel(ModelBakeSettings settings, ModelSettings itemBakeSettings, List<BlockModelPart> bakedModels) {
        super(settings, itemBakeSettings, bakedModels);
    }

    public void pushTextureTransform(QuadEmitter context, Sprite sprite) {
        context.pushTransform(quad -> {
            Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getAtlasManager().getAtlasTexture(Atlases.BLOCKS)).find(quad);
            if (originalSprite.getContents().getId() != sprite.getContents().getId()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = ModelHelper.getFrameFromU(originalSprite, quad.u(index));
                    float frameV = ModelHelper.getFrameFromV(originalSprite, quad.v(index));
                    quad.uv(index, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(QuadEmitter context, List<Sprite> toReplace, List<Sprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, Atlases.BLOCKS);
    }
    public void pushTextureTransform(QuadEmitter context, List<Sprite> toReplace, List<Sprite> replacement, Identifier atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                Sprite originalSprite = SpriteFinder.get(MinecraftClient.getInstance().getAtlasManager().getAtlasTexture(atlasId)).find(quad, 0);
                Identifier keyId = originalSprite.getContents().getId();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).getContents().getId()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    Sprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = ModelHelper.getFrameFromU(originalSprite, quad.u(index));
                        float frameV = ModelHelper.getFrameFromV(originalSprite, quad.v(index));
                        quad.uv(index, sprite.getFrameU(frameU), sprite.getFrameV(frameV));
                    }
                }
            }
            return true;
        });
    }


    @Override
    public Sprite pfm$getParticle(World world, BlockPos pos, BlockState state) {
        return pfm$getParticle(state);
    }

    @Override
    public Sprite particleSprite() {
        return getTemplateBakedModels().get(0).particleSprite();
    }

    @Override
    public Sprite particleSprite(BlockRenderView blockView, BlockPos pos, BlockState state) {
        return pfm$getParticle(state);
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


    public void emitModelQuads(QuadEmitter emitter, BlockStateModel model, Random random) {
        List<BlockModelPart> parts = model.getParts(random);
        int partCount = parts.size();

        for(int i = 0; i < partCount; ++i) {
            parts.get(i).emitQuads(emitter, null);
        }
    }

    abstract public void emitItemQuads(QuadEmitter context, Random randomSupplier);
}
