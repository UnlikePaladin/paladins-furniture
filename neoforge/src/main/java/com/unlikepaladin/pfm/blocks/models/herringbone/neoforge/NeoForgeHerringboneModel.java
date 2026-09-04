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
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeoForgeHerringboneModel extends PFMNeoForgeBakedModel {
    public NeoForgeHerringboneModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (state == null || !(state.getBlock() instanceof HerringbonePlankBlock)) {
            return super.particleMaterial(level, pos, state);
        }
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return new Material.Baked(generateTextureIfNeeded(variant), false);
        }
        return super.particleMaterial(level, pos, state);
    }

    static SpriteId herringboneTextureId = new SpriteId(TextureAtlas.LOCATION_BLOCKS, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
                List<BlockStateModelPart> quads = new ArrayList<>(getTemplateBakedModels());
                parts.addAll(getPartsWithTexture(quads, new SpriteData(replacement)));
            }
        }
    }

    private TextureAtlasSprite generateTextureIfNeeded(VariantBase<?> variant) {
        Identifier finalId = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteId mainTexture = new SpriteId(TextureAtlas.LOCATION_BLOCKS, finalId);
        if (!((PFMSpriteContentExtensions)(ModelHelper.getSprite(mainTexture)).contents()).pfm$isInitialized()) {
            SpriteId baseTextureSpriteId = new SpriteId(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.PRIMARY));
            ModelHelper.generateTexture(ModelHelper.getSprite(herringboneTextureId), ModelHelper.getSprite(baseTextureSpriteId), 7, finalId);
        }
        return ModelHelper.getSprite(mainTexture);
    }

    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, RandomSource random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && !((PFMSpriteContentExtensions)cache.get(directionPair).get(0).materialInfo().sprite().contents()).pfm$isInitialized()) {
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
            for (BlockStateModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return Collections.emptyList();
    }
}
