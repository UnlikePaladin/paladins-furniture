package com.unlikepaladin.pfm.blocks.models.classicCoffeeTable.fabric;

import com.unlikepaladin.pfm.blocks.ClassicCoffeeTableBlock;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FabricClassicCoffeeTableModel extends PFMFabricBakedModel {
    public FabricClassicCoffeeTableModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof ClassicCoffeeTableBlock) {
            ClassicCoffeeTableBlock block = (ClassicCoffeeTableBlock) state.getBlock();
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
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
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
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}