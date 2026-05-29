package com.unlikepaladin.pfm.blocks.models.herringbone.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricHerringboneModel extends PFMFabricBakedModel {
    public FabricHerringboneModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            ResourceLocation finalId = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
            Material mainTexture = new Material(TextureAtlas.LOCATION_BLOCKS, finalId);
            if (!((PFMSpriteContentExtensions)(ModelHelper.getSprite(mainTexture)).contents()).pfm$isInitialized()) {
                Material baseTextureSpriteId = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.PRIMARY));
                ModelHelper.generateTexture(ModelHelper.getSprite(herringboneTextureId), ModelHelper.getSprite(baseTextureSpriteId), 7, finalId);
            }
            return ModelHelper.getSprite(mainTexture);
        }
        return super.particleIcon();
    }

    static Material herringboneTextureId = new Material(TextureAtlas.LOCATION_BLOCKS, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            generateTextureIfNeeded(emitter, variant);
            for (BlockModelPart model : getTemplateBakedModels()) {
                model.emitQuads(emitter, cullTest);
            }
            emitter.popTransform();
        }
    }

    private void generateTextureIfNeeded(QuadEmitter context, VariantBase<?> variant) {
        ResourceLocation finalId = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        Material mainTexture = new Material(TextureAtlas.LOCATION_BLOCKS, finalId);
        if (!((PFMSpriteContentExtensions)(ModelHelper.getSprite(mainTexture)).contents()).pfm$isInitialized()) {
            Material baseTextureSpriteId = new Material(TextureAtlas.LOCATION_BLOCKS, variant.getTextureLocation(BlockType.PRIMARY));
            ModelHelper.generateTexture(ModelHelper.getSprite(herringboneTextureId), ModelHelper.getSprite(baseTextureSpriteId), 7, finalId);
        }
        pushTextureTransform(context, ModelHelper.getSprite(mainTexture));
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, RandomSource randomSupplier) {
        if (blockState == null) return;

        VariantBase<?> variant = getVariant(blockState);
        if (variant instanceof WoodVariant) {
            generateTextureIfNeeded(emitter, variant);
            for (BlockModelPart model : getTemplateBakedModels()) {
                model.emitQuads(emitter, any -> false);
            }
            emitter.popTransform();
        }
    }
}
