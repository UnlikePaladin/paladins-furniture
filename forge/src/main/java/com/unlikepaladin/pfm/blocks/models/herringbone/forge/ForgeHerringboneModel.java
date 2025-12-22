package com.unlikepaladin.pfm.blocks.models.herringbone.forge;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForgeHerringboneModel extends PFMForgeBakedModel {
    public ForgeHerringboneModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @Override
    public Sprite getParticleIcon(@NotNull IModelData data) {
        if (!data.hasProperty(STATE) || data.getData(STATE) == null) {
            return super.getParticleIcon(data);
        }
        BlockState state = data.getData(STATE);
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            return generateTextureIfNeeded(variant);
        }
        return super.getParticleIcon(data);
    }

    @Override
    public @NotNull IModelData getModelData(@NotNull BlockRenderView world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData tileData) {
        if (state != null) {
            ModelDataMap.Builder builder = new ModelDataMap.Builder();
            appendProperties(builder);
            IModelData data = builder.build();
            super.getModelData(world, pos, state, data);
            return data;
        }
        return super.getModelData(world, pos, state, tileData);
    }

    static SpriteIdentifier herringboneTextureId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PFMSpriteRegistry.HERRINGBONE_PLANKS);

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                Sprite replacement = generateTextureIfNeeded(variant);
                List<BakedQuad> quads = new ArrayList<>();
                for (BakedModel model : getTemplateBakedModels()) {
                    quads.addAll(model.getQuads(state, side, rand, extraData));
                }
                return getQuadsWithTexture(quads, new SpriteData(replacement));
            }
        }
        return super.getQuads(state, side, rand, extraData);
    }

    private Sprite generateTextureIfNeeded(VariantBase<?> variant) {
        Identifier finalId = new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteIdentifier mainTexture = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, finalId);
        if (mainTexture.getSprite().getId() == MissingSprite.getMissingSpriteId()) {
            SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
        }
        return mainTexture.getSprite();
    }

    @Override
    public List<BakedQuad> getQuadsCached(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        Pair<ItemStack, Direction> directionPair = new Pair<>(stack, face);
        if (cache.containsKey(directionPair) && !cache.get(directionPair).isEmpty() && cache.get(directionPair).get(0).getSprite().getId() == MissingSprite.getMissingSpriteId()) {
            cache.remove(directionPair);
        }
        return super.getQuadsCached(stack, state, face, random);
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, Random random) {
        VariantBase<?> variant = getVariant(((BlockItem) stack.getItem()).getBlock().getDefaultState());
        if (variant instanceof WoodVariant) {
            Sprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BakedModel model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(state, face, random));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return super.getQuads(stack, state, face, random);
    }
}
