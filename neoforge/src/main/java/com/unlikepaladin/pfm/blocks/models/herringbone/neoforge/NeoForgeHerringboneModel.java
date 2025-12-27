package com.unlikepaladin.pfm.blocks.models.herringbone.neoforge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Atlases;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeoForgeHerringboneModel extends PFMNeoForgeBakedModel {
    public NeoForgeHerringboneModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public Sprite particleIcon(BlockRenderView level, BlockPos pos, BlockState state) {
        if (state == null) {
            return super.particleIcon(level, pos, state);
        }
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return generateTextureIfNeeded(variant);
        }
        return super.particleIcon(level, pos, state);
    }

    static SpriteIdentifier herringboneTextureId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void collectParts(BlockRenderView world, BlockPos pos, BlockState state, Random random, List<BlockModelPart> parts) {
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                Sprite replacement = generateTextureIfNeeded(variant);
                List<BlockModelPart> quads = new ArrayList<>(getTemplateBakedModels());
                parts.addAll(getPartsWithTexture(quads, new SpriteData(replacement)));
            }
        }
    }

    private Sprite generateTextureIfNeeded(VariantBase<?> variant) {
        Identifier finalId = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteIdentifier mainTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, finalId);
        if (!((PFMSpriteContentExtensions)(ModelHelper.getSprite(mainTexture)).getContents()).pfm$isInitialized()) {
            SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            ModelHelper.generateTexture(ModelHelper.getSprite(herringboneTextureId), ModelHelper.getSprite(baseTextureSpriteId), 7, finalId);
        }
        return ModelHelper.getSprite(mainTexture);
    }

    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && !((PFMSpriteContentExtensions)cache.get(directionPair).get(0).sprite().getContents()).pfm$isInitialized()) {
            cache.remove(directionPair);
        }
        return super.getQuadsCached(face, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        if (blockState == null) {
            return Collections.emptyList();
        }

        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            Sprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return Collections.emptyList();
    }
}
