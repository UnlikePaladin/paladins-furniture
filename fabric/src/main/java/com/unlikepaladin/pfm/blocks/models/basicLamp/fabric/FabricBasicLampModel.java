package com.unlikepaladin.pfm.blocks.models.basicLamp.fabric;

import com.unlikepaladin.pfm.blocks.BasicLampBlock;
import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import com.unlikepaladin.pfm.data.materials.BlockType;
import com.unlikepaladin.pfm.data.materials.WoodVariant;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import com.unlikepaladin.pfm.items.PFMComponents;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Supplier;
import net.minecraft.util.RandomSource;

public class FabricBasicLampModel extends PFMFabricBakedModel {
    public FabricBasicLampModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        BlockEntity entity = blockView.getBlockEntity(pos);
        int onOffset = state.getValue(BlockStateProperties.LIT) ? 1 : 0;
        if (entity instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        boolean up = blockView.getBlockState(pos.above()).getBlock() instanceof BasicLampBlock;
        boolean down = blockView.getBlockState(pos.below()).getBlock() instanceof BasicLampBlock;
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        if (up && down) {
            (getTemplateBakedModels().get(1)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (up) {
            (getTemplateBakedModels().get(0)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else if (down)
        {
            (getTemplateBakedModels().get(3)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            (getTemplateBakedModels().get(5+onOffset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            (getTemplateBakedModels().get(4)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
        else {
            (getTemplateBakedModels().get(4)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            (getTemplateBakedModels().get(2)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
            (getTemplateBakedModels().get(5+onOffset)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
        context.popTransform();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (stack.get(PFMComponents.VARIANT_COMPONENT) != null) {
            variant = WoodVariantRegistry.getVariant(stack.get(PFMComponents.VARIANT_COMPONENT));
        }
        pushTextureTransform(context, getOakStrippedLogSprite(), getVariantStrippedLogSprite(variant));
        (getTemplateBakedModels().get(4)).emitItemQuads(stack, randomSupplier, context);
        (getTemplateBakedModels().get(2)).emitItemQuads(stack, randomSupplier, context);
        (getTemplateBakedModels().get(5)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public ItemTransforms getTransforms() {
        return getTemplateBakedModels().get(2).getTransforms();
    }

    static List<TextureAtlasSprite> oakSprite = new ArrayList<>();
    static List<TextureAtlasSprite> getOakStrippedLogSprite() {
        if (!oakSprite.isEmpty())
            return oakSprite;
        TextureAtlasSprite wood = new Material(InventoryMenu.BLOCK_ATLAS,  new ResourceLocation("minecraft:block/stripped_oak_log")).sprite();
        oakSprite.add(wood);
        return oakSprite;
    }

    Map<WoodVariant, List<TextureAtlasSprite>> sprites = new HashMap<>();
    List<TextureAtlasSprite> getVariantStrippedLogSprite(WoodVariant variant) {
        if (sprites.containsKey(variant))
            return sprites.get(variant);

        TextureAtlasSprite wood = new Material(InventoryMenu.BLOCK_ATLAS, variant.getTextureLocation(BlockType.STRIPPED_LOG)).sprite();
        List<TextureAtlasSprite> spriteList = new ArrayList<>();
        spriteList.add(wood);
        sprites.put(variant, spriteList);
        return spriteList;
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getTemplateBakedModels().get(4).getParticleIcon();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(Level world, BlockPos pos, BlockState state) {
        BlockEntity entity = world.getBlockEntity(pos);
        WoodVariant variant = WoodVariantRegistry.OAK;
        if (world.getBlockEntity(pos) instanceof LampBlockEntity) {
            variant = ((LampBlockEntity) entity).getVariant();
        }
        return getVariantStrippedLogSprite(variant).get(0);
    }
}
