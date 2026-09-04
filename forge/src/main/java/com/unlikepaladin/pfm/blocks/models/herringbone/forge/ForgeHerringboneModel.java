package com.unlikepaladin.pfm.blocks.models.herringbone.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForgeHerringboneModel extends PFMForgeBakedModel {
    public ForgeHerringboneModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockStateModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public Material.Baked particleMaterial(@NotNull ModelData data) {
        if (!data.has(STATE) || data.get(STATE) == null) {
            return super.particleMaterial(data);
        }
        BlockState state = data.get(STATE);
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return new Material.Baked(generateTextureIfNeeded(variant), false);
        }
        return super.particleMaterial(data);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state != null) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            return data;
        }
        return super.getModelData(world, pos, state, tileData);
    }

    static SpriteId herringboneTextureId = new SpriteId(TextureAtlas.LOCATION_BLOCKS, PFMSpriteRegistry.HERRINGBONE_PLANKS);

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> dest, ModelData data) {
        BlockState state = data.get(STATE);
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
                for (BlockStateModelPart model : getTemplateBakedModels()) {
                    dest.add(getPartWithTexture(model, new SpriteData(replacement)));
                }
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
        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockStateModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return List.of();
    }
}
