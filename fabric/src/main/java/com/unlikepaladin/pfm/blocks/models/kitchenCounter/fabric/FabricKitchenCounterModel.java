package com.unlikepaladin.pfm.blocks.models.kitchenCounter.fabric;

import com.unlikepaladin.pfm.blocks.KitchenCounterBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
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
    public void emitBlockQuads(QuadEmitter context, BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
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
                        getTemplateBakedModels().get((5)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    }
                    else {
                        getTemplateBakedModels().get((6)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
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
                        getTemplateBakedModels().get((4)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
                    } else {
                        getTemplateBakedModels().get((3)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
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

    private void middleCounter(BlockAndTintGetter world, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean left, boolean right) {
        if (left && right) {
            getTemplateBakedModels().get((0)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (left) {
            getTemplateBakedModels().get((1)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else if (right) {
            getTemplateBakedModels().get((2)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        } else {
            getTemplateBakedModels().get((0)).emitBlockQuads(context, world, state, pos, randomSupplier, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Supplier<RandomSource> randomSupplier) {
        if (blockState == null) return;

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((0)).emitItemQuads(context, randomSupplier);
        context.popTransform();
    }

    @Override
    public TextureAtlasSprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}