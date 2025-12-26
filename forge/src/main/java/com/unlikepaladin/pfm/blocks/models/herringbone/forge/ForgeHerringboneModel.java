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
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.MissingSprite;
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
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForgeHerringboneModel extends PFMForgeBakedModel {
    public ForgeHerringboneModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public Sprite particleIcon(@NotNull ModelData data) {
        if (!data.has(STATE) || data.get(STATE) == null) {
            return super.particleIcon(data);
        }
        BlockState state = data.get(STATE);
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return generateTextureIfNeeded(variant);
        }
        return super.particleIcon(data);
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
    public void collectParts(Random random, List<BlockModelPart> dest, ModelData data, @Nullable RenderLayer renderType) {
        BlockState state = data.get(STATE);
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                Sprite replacement = generateTextureIfNeeded(variant);
                for (BlockModelPart model : getTemplateBakedModels()) {
                    dest.add(getPartWithTexture(model, new SpriteData(replacement)));
                }
            }
        }
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
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && !((PFMSpriteContentExtensions)cache.get(directionPair).get(0).sprite().getContents()).pfm$isInitialized()) {
            cache.remove(directionPair);
        }
        return super.getQuadsCached(face, random);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, Random random) {
        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            Sprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return List.of();
    }
}
