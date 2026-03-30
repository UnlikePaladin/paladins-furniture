package com.unlikepaladin.pfm.blocks.models.herringbone.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.client.PFMSpriteRegistry;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.VariantBase;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.ducks.PFMSpriteContentExtensions;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.function.Supplier;

public class FabricHerringboneModel extends PFMFabricBakedModel {
    public FabricHerringboneModel(ModelState settings, List<BakedModel> templateBakedModels) {
        super(settings, templateBakedModels);
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        VariantBase<?> variant = getVariant(state);
        if (variant instanceof WoodVariant) {
            ResourceLocation finalId = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
            Material mainTexture = new Material(InventoryMenu.BLOCK_ATLAS, finalId);
            if (!((PFMSpriteContentExtensions)mainTexture.sprite().contents()).pfm$isInitialized()) {
                Material baseTextureSpriteId = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.PRIMARY));
                ModelHelper.generateTexture(herringboneTextureId.sprite(), baseTextureSpriteId.sprite(), 7, finalId);
            }
            return mainTexture.sprite();
        }
        return super.getParticleIcon();
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    static Material herringboneTextureId = new Material(InventoryMenu.BLOCK_ATLAS, PFMSpriteRegistry.HERRINGBONE_PLANKS);
    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
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
        ResourceLocation finalId = new ResourceLocation(PaladinFurnitureMod.MOD_ID, "block/" + variant.getIdentifier().getPath() + "_herringbone_planks");
        Material mainTexture = new Material(InventoryMenu.BLOCK_ATLAS, finalId);
        if (!((PFMSpriteContentExtensions)mainTexture.sprite().contents()).pfm$isInitialized()) {
            Material baseTextureSpriteId = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.PRIMARY));
            ModelHelper.generateTexture(herringboneTextureId.sprite(), baseTextureSpriteId.sprite(), 7, finalId);
        }
        pushTextureTransform(context, mainTexture.sprite());
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (!(stack.getItem() instanceof BlockItem)) return;

        VariantBase<?> variant = getVariant(((BlockItem) stack.getItem()).getBlock().defaultBlockState());
        if (variant instanceof WoodVariant) {
            generateTextureIfNeeded(context, variant);
            for (BakedModel model : getTemplateBakedModels()) {
                ((FabricBakedModel)model).emitItemQuads(stack, randomSupplier, context);
            }
            context.popTransform();
        }
    }
}
