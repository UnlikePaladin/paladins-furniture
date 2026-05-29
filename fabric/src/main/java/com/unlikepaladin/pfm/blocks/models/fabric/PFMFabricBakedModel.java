package com.unlikepaladin.pfm.blocks.models.fabric;

import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.client.fabric.PFMBakedModelParticleExtension;
import com.unlikepaladin.pfm.client.model.PFMBakedModelSetPropertiesExtension;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.IntStream;

public abstract class PFMFabricBakedModel extends AbstractBakedModel implements FabricBlockStateModel, PFMBakedModelParticleExtension, PFMBakedModelSetPropertiesExtension {
    protected BlockState blockState;
    protected VariantBase<?> variant;

    public PFMFabricBakedModel(ModelState settings, ModelRenderProperties itemBakeSettings, List<BlockModelPart> bakedModels) {
        super(settings, itemBakeSettings, bakedModels);
    }

    public void pushTextureTransform(QuadEmitter context, TextureAtlasSprite sprite) {
        context.pushTransform(quad -> {
            TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS)).find(quad);
            if (originalSprite.contents().name() != sprite.contents().name()) {
                for (int index = 0; index < 4; index++) {
                    float frameU = ModelHelper.getUOffset(originalSprite, quad.u(index));
                    float frameV = ModelHelper.getVOffset(originalSprite, quad.v(index));
                    quad.uv(index, sprite.getU(frameU), sprite.getV(frameV));
                }
            }
            return true;
        });
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement) {
        pushTextureTransform(context, toReplace, replacement, AtlasIds.BLOCKS);
    }
    public void pushTextureTransform(QuadEmitter context, List<TextureAtlasSprite> toReplace, List<TextureAtlasSprite> replacement, ResourceLocation atlasId) {
        context.pushTransform(quad -> {
            if (replacement != null && toReplace != null ){
                TextureAtlasSprite originalSprite = SpriteFinder.get(Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(atlasId)).find(quad, 0);
                ResourceLocation keyId = originalSprite.contents().name();
                int textureIndex = IntStream.range(0, toReplace.size())
                        .filter(i -> keyId.equals(toReplace.get(i).contents().name()))
                        .findFirst()
                        .orElse(-1);

                if (textureIndex != -1 && !toReplace.equals(replacement)) {
                    TextureAtlasSprite sprite = replacement.get(textureIndex);
                    for (int index = 0; index < 4; index++) {
                        float frameU = ModelHelper.getUOffset(originalSprite, quad.u(index));
                        float frameV = ModelHelper.getVOffset(originalSprite, quad.v(index));
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
    public TextureAtlasSprite particleIcon() {
        return getTemplateBakedModels().get(0).particleIcon();
    }

    @Override
    public TextureAtlasSprite particleSprite(BlockAndTintGetter blockView, BlockPos pos, BlockState state) {
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


    public void emitModelQuads(QuadEmitter emitter, BlockStateModel model, RandomSource random) {
        List<BlockModelPart> parts = model.collectParts(random);
        int partCount = parts.size();

        for(int i = 0; i < partCount; ++i) {
            parts.get(i).emitQuads(emitter, null);
        }
    }

    abstract public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier);
}
