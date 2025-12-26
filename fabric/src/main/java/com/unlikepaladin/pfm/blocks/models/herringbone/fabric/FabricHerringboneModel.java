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
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricHerringboneModel extends PFMFabricBakedModel {
    public FabricHerringboneModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            Identifier finalId = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
            SpriteIdentifier mainTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, finalId);
            if (!((PFMSpriteContentExtensions)mainTexture.getSprite().getContents()).pfm$isInitialized()) {
                SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
                ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
            }
            return mainTexture.getSprite();
        }
        return super.particleSprite();
    }

    static SpriteIdentifier herringboneTextureId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
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
        Identifier finalId = Identifier.of(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteIdentifier mainTexture = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, finalId);
        if (!((PFMSpriteContentExtensions)mainTexture.getSprite().getContents()).pfm$isInitialized()) {
            SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
        }
        pushTextureTransform(context, mainTexture.getSprite());
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Random randomSupplier) {
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
