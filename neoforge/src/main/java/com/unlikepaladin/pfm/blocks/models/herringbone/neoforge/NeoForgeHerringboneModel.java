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
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeHerringboneModel extends PFMNeoForgeBakedModel {
    public NeoForgeHerringboneModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @Override
    public Sprite getParticleIcon(@NotNull ModelData data) {
        if (!data.has(STATE) || data.get(STATE) == null) {
            return super.getParticleIcon(data);
        }
        BlockState state = data.get(STATE);
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return generateTextureIfNeeded(variant);
        }
        return super.getParticleIcon(data);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state != null) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            return data;
        }
        return super.getModelData(world, pos, state, tileData);
    }

    static SpriteIdentifier herringboneTextureId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, PFMSpriteRegistry.HERRINGBONE_PLANKS);

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull ModelData extraData, RenderLayer renderLayer) {
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                Sprite replacement = generateTextureIfNeeded(variant);
                List<BakedQuad> quads = new ArrayList<>();
                for (BakedModel model : getTemplateBakedModels()) {
                    quads.addAll(model.getQuads(state, side, rand, extraData, renderLayer));
                }
                return getQuadsWithTexture(quads, new SpriteData(replacement));
            }
        }
        return super.getQuads(state, side, rand, extraData, renderLayer);
    }

    private Sprite generateTextureIfNeeded(VariantBase<?> variant) {
        Identifier finalId = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteIdentifier mainTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, finalId);
        if (!((PFMSpriteContentExtensions)mainTexture.getSprite().getContents()).pfm$isInitialized()) {
            SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
        }
        return mainTexture.getSprite();
    }

    @Override
    public List<BakedQuad> getQuadsCached(@Nullable Direction face, Random random) {
        Pair<BlockState, Direction> directionPair = new Pair<>(blockState, face);
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && !((PFMSpriteContentExtensions)cache.get(directionPair).get(0).getSprite().getContents()).pfm$isInitialized()) {
            cache.remove(directionPair);
        }
        return super.getQuadsCached(face, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        if (blockState == null) {
            return super.getQuads(face, random);
        }

        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            Sprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BakedModel model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(null, face, random));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return super.getQuads(face, random);
    }
}
