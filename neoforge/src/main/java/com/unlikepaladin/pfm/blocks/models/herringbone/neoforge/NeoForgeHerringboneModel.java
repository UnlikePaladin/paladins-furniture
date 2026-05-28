package com.unlikepaladin.pfm.blocks.models.herringbone.neoforge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.HerringbonePlankBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeoForgeHerringboneModel extends PFMNeoForgeBakedModel {
    public NeoForgeHerringboneModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public TextureAtlasSprite particleIcon(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (state == null || !(state.getBlock() instanceof HerringbonePlankBlock)) {
            return super.particleIcon(level, pos, state);
        }
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return generateTextureIfNeeded(variant);
        }
        return super.particleIcon(level, pos, state);
    }

    static Material herringboneTextureId = new Material(TextureAtlas.LOCATION_BLOCKS, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
                List<BlockModelPart> quads = new ArrayList<>(getTemplateBakedModels());
                parts.addAll(getPartsWithTexture(quads, new SpriteData(replacement)));
            }
        }
    }

    private TextureAtlasSprite generateTextureIfNeeded(VariantBase<?> variant) {
        ResourceLocation finalId = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        Material mainTexture = new Material(TextureAtlas.LOCATION_BLOCKS, finalId);
        if (!((PFMSpriteContentExtensions)mainTexture.sprite().contents()).pfm$isInitialized()) {
            Material baseTextureSpriteId = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.sprite(), baseTextureSpriteId.sprite(), 7, finalId);
        }
        return mainTexture.sprite();
    }

    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && !((PFMSpriteContentExtensions)cache.get(directionPair).get(0).sprite().contents()).pfm$isInitialized()) {
            cache.remove(directionPair);
        }
        return super.getQuadsCached(face, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        if (blockState == null) {
            return Collections.emptyList();
        }

        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return Collections.emptyList();
    }
}
