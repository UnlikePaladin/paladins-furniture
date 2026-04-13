package com.unlikepaladin.pfm.blocks.models.kitchenCounter.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCounterBlock;
import com.unlikepaladin.pfm.blocks.models.AbstractBakedModel;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import java.util.function.Supplier;

public class FabricKitchenCounterModel extends PFMFabricBakedModel {
    public FabricKitchenCounterModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof KitchenCounterBlock) {
            KitchenCounterBlock block = (KitchenCounterBlock) state.getBlock();
            Direction direction = state.getValue(KitchenCounterBlock.FACING);
            boolean right = block.canConnect(world, pos, direction.getCounterClockWise());
            boolean left = block.canConnect(world, pos, direction.getClockWise());
            BlockState neighborStateFacing = world.getBlockState(pos.relative(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(direction.getOpposite()));
            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (direction2.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                    if (direction2 == direction.getCounterClockWise()) {
                        ((FabricBakedModel) getTemplateBakedModels().get((5))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                    else {
                        ((FabricBakedModel) getTemplateBakedModels().get((6))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, left, right);
                }
            }
            else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                }
                if (direction3.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3)) {
                    if (direction3 == direction.getCounterClockWise()) {
                        ((FabricBakedModel) getTemplateBakedModels().get((4))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    } else {
                        ((FabricBakedModel) getTemplateBakedModels().get((3))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, left, right);
                }
            }
            else {
                middleCounter(world, state, pos, randomSupplier, context, left, right);
            }
            context.popTransform();
        }
    }

    private void middleCounter(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, boolean left, boolean right) {
        if (left && right) {
            ((FabricBakedModel) getTemplateBakedModels().get((0))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (left) {
            ((FabricBakedModel) getTemplateBakedModels().get((1))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (right) {
            ((FabricBakedModel) getTemplateBakedModels().get((2))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get((0))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }
    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        List<TextureAtlasSprite> spriteList = getSpriteList(stack);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        ((FabricBakedModel) getTemplateBakedModels().get((0))).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}