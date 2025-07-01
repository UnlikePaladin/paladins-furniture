package com.unlikepaladin.pfm.blocks.models.kitchenDrawer.fabric;

import com.unlikepaladin.pfm.blocks.KitchenDrawerBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;

import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricKitchenDrawerModel extends PFMFabricBakedModel {
    public FabricKitchenDrawerModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof KitchenDrawerBlock) {
            KitchenDrawerBlock block = (KitchenDrawerBlock) state.getBlock();
            Direction direction = state.get(KitchenDrawerBlock.FACING);
            boolean right = block.canConnect(world, pos, direction.rotateYCounterclockwise());
            boolean left = block.canConnect(world, pos, direction.rotateYClockwise());
            BlockState neighborStateFacing = world.getBlockState(pos.offset(direction));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(direction.getOpposite()));
            int openOffset = state.get(KitchenDrawerBlock.OPEN) ? 7 : 0;
            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);

            if (block.canConnectToCounter(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction2 = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                // outer corner
                if (direction2.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
                    if (direction2 == direction.rotateYCounterclockwise()) {
                        getTemplateBakedModels().get((5 + openOffset)).emitQuads(context, cullTest);
                    }
                    else {
                        getTemplateBakedModels().get((6 + openOffset)).emitQuads(context, cullTest);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else if (block.canConnectToCounter(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction direction3;
                if (neighborStateOpposite.getBlock() instanceof AbstractFurnaceBlock) {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING).getOpposite();
                }
                else {
                    direction3 = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                }
                if (direction3.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(state, world, pos, direction3)) {
                    // inner corner
                    if (direction3 == direction.rotateYCounterclockwise()) {
                        getTemplateBakedModels().get((4 + openOffset)).emitQuads(context, cullTest);
                    } else {
                        getTemplateBakedModels().get((3 + openOffset)).emitQuads(context, cullTest);
                    }
                } else {
                    middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else {
                middleCounter(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
            }
            context.popTransform();
        }
    }

    private void middleCounter(BlockRenderView world, BlockState state, BlockPos pos, Random randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean left, boolean right, int openOffset) {
        if (left && right) {
            getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
        } else if (left) {
            getTemplateBakedModels().get((1 + openOffset)).emitQuads(context, cullTest);
        } else if (right) {
            getTemplateBakedModels().get((2 + openOffset)).emitQuads(context, cullTest);
        } else {
            getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;
        List<Sprite> spriteList = getSpriteList(blockState);
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), spriteList);
        getTemplateBakedModels().get((0)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}