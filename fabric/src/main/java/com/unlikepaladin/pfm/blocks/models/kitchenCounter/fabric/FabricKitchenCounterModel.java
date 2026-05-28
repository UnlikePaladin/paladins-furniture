package com.unlikepaladin.pfm.blocks.models.kitchenCounter.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCounterBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.List;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FabricKitchenCounterModel extends PFMFabricBakedModel {
    public FabricKitchenCounterModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource randomSupplier, Predicate<@Nullable Direction> cullTest) {
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
                        getTemplateBakedModels().get((5)).emitQuads(context, cullTest);
                    }
                    else {
                        getTemplateBakedModels().get((6)).emitQuads(context, cullTest);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right);
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
                        getTemplateBakedModels().get((4)).emitQuads(context, cullTest);
                    } else {
                        getTemplateBakedModels().get((3)).emitQuads(context, cullTest);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right);
                }
            }
            else {
                middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right);
            }
            context.popTransform();
        }
    }

    private void middleCounter(BlockAndTintGetter world, BlockState state, BlockPos pos, RandomSource randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean left, boolean right) {
        if (left && right) {
            getTemplateBakedModels().get((0)).emitQuads(context, cullTest);
        } else if (left) {
            getTemplateBakedModels().get((1)).emitQuads(context, cullTest);
        } else if (right) {
            getTemplateBakedModels().get((2)).emitQuads(context, cullTest);
        } else {
            getTemplateBakedModels().get((0)).emitQuads(context, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, RandomSource randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((0)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}