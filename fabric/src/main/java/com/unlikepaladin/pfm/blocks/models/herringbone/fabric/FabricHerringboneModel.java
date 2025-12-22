package com.unlikepaladin.pfm.blocks.models.herringbone.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FabricHerringboneModel extends PFMFabricBakedModel {
    public FabricHerringboneModel(ModelBakeSettings settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            Identifier finalId = new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
            SpriteIdentifier mainTexture = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, finalId);
            if (mainTexture.getSprite().getId() == MissingSprite.getMissingSpriteId()) {
                SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
                ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
            }
            return mainTexture.getSprite();
        }
        return super.getParticleSprite();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    static SpriteIdentifier herringboneTextureId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            generateTextureIfNeeded(context, variant);
            for (BakedModel model : getTemplateBakedModels()) {
                ((FabricBakedModel)model).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
            context.popTransform();
        }
    }

    private void generateTextureIfNeeded(RenderContext context, VariantBase<?> variant) {
        Identifier finalId = new Identifier(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        SpriteIdentifier mainTexture = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, finalId);
        if (mainTexture.getSprite().getId() == MissingSprite.getMissingSpriteId()) {
            SpriteIdentifier baseTextureSpriteId = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, variant.getTexture(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.getSprite(), baseTextureSpriteId.getSprite(), 7, finalId);
        }
        pushTextureTransform(context, mainTexture.getSprite());
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        if (!(stack.getItem() instanceof BlockItem)) return;

        VariantBase<?> variant = getVariant(((BlockItem) stack.getItem()).getBlock().getDefaultState());
        if (variant instanceof WoodVariant) {
            generateTextureIfNeeded(context, variant);
            for (BakedModel model : getTemplateBakedModels()) {
                ((FabricBakedModel)model).emitItemQuads(stack, randomSupplier, context);
            }
            context.popTransform();
        }
    }
}
