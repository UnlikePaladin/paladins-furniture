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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ForgeHerringboneModel extends PFMForgeBakedModel {
    public ForgeHerringboneModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public TextureAtlasSprite particleIcon(@NotNull ModelData data) {
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
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state != null) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);
            return data;
        }
        return super.getModelData(world, pos, state, tileData);
    }

    static Material herringboneTextureId = new Material(TextureAtlas.LOCATION_BLOCKS, PFMSpriteRegistry.HERRINGBONE_PLANKS);

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> dest, ModelData data, @Nullable ChunkSectionLayer renderType) {
        BlockState state = data.get(STATE);
        if (state != null) {
            VariantBase<?> variant = getVariant(state);
            if (variant instanceof WoodVariant) {
                TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
                for (BlockModelPart model : getTemplateBakedModels()) {
                    dest.add(getPartWithTexture(model, new SpriteData(replacement)));
                }
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
        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            TextureAtlasSprite replacement = generateTextureIfNeeded(variant);
            List<BakedQuad> quads = new ArrayList<>();
            for (BlockModelPart model : getTemplateBakedModels()) {
                quads.addAll(model.getQuads(face));
            }
            return getQuadsWithTexture(quads, new SpriteData(replacement));
        }
        return List.of();
    }
}
