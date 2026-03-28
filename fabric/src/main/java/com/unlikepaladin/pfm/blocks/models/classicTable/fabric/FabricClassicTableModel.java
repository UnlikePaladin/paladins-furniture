package com.unlikepaladin.pfm.blocks.models.classicTable.fabric;

import com.unlikepaladin.pfm.blocks.ClassicTableBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import java.util.function.Supplier;

public class FabricClassicTableModel extends PFMFabricBakedModel {
    public FabricClassicTableModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof ClassicTableBlock) {
            ClassicTableBlock block = (ClassicTableBlock) state.getBlock();
            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            pushTextureTransform(context, getSpriteList(state).get(0));
            ((FabricBakedModel) getTemplateBakedModels().get(0)).emitBlockQuads(world, state, pos, randomSupplier, context);
            context.popTransform();

            pushTextureTransform(context, getSpriteList(state).get(1));
            if (!north && !east) {
                ((FabricBakedModel) getTemplateBakedModels().get(1)).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!north && !west) {
                ((FabricBakedModel) getTemplateBakedModels().get(2)).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!south && !west) {
                ((FabricBakedModel) getTemplateBakedModels().get(3)).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            if (!south && !east) {
                ((FabricBakedModel) getTemplateBakedModels().get(4)).emitBlockQuads(world, state, pos, randomSupplier, context);
            }
            context.popTransform();
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        pushTextureTransform(context, getSpriteList(stack).get(0));
        ((FabricBakedModel) getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();

        pushTextureTransform(context, getSpriteList(stack).get(1));
        // legs
        ((FabricBakedModel) getTemplateBakedModels().get(1)).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel) getTemplateBakedModels().get(2)).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel) getTemplateBakedModels().get(3)).emitItemQuads(stack, randomSupplier, context);
        ((FabricBakedModel) getTemplateBakedModels().get(4)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}