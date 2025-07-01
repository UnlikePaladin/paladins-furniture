package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.fabric;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class FabricBasicDeskCabinetModel extends PFMFabricBakedModel {
    public FabricBasicDeskCabinetModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random randomSupplier, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof BasicDeskCabinetBlock block) {
            Direction isFacing = state.get(BasicDeskCabinetBlock.FACING);

            BlockState rightState = world.getBlockState(pos.offset(isFacing.rotateYCounterclockwise()));
            boolean right = block.canConnect(rightState) && rightState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.offset(isFacing.rotateYClockwise()));
            boolean left = block.canConnect(leftState) && leftState.getBlock() instanceof BasicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.offset(isFacing));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(isFacing.getOpposite()));
            int openOffset = state.get(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));
            if (block.canConnect(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                // outer corner
                if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(world, pos, neighborFacing.getOpposite())) {
                    if (neighborFacing == isFacing.rotateYCounterclockwise()) {
                        getTemplateBakedModels().get((4 + openOffset)).emitQuads(context, cullTest);
                    }
                    else {
                        getTemplateBakedModels().get((5 + openOffset)).emitQuads(context, cullTest);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(world, pos, neighborFacing)) {
                    if (neighborFacing == isFacing.rotateYCounterclockwise()) {
                        getTemplateBakedModels().get((6 + openOffset)).emitQuads(context, cullTest);
                    } else {
                        getTemplateBakedModels().get((7 + openOffset)).emitQuads(context, cullTest);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
                }
            }
            else {
                middleDesk(world, state, pos, randomSupplier, context, cullTest, left, right, openOffset);
            }


            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            switch (isFacing) {
                case SOUTH:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 18, 19, 16, 17);
                        break;
                case NORTH:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 17, 16, 19, 18);
                        break;
                case EAST:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 19, 17, 18, 16);
                        break;
                default:
                        legsDesk(world, state, pos, randomSupplier, context, cullTest, north, south, east, west, 16, 18, 17, 19);
                        break;
            }
            context.popTransform();
        }
    }

    private void legsDesk(BlockRenderView world, BlockState state, BlockPos pos, Random randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        if (!north && !east) {
            getTemplateBakedModels().get(northLeg).emitQuads(context, cullTest);
        }
        if (!north && !west) {
            getTemplateBakedModels().get(southLeg).emitQuads(context, cullTest);
        }
        if (!south && !west) {
            getTemplateBakedModels().get(eastLeg).emitQuads(context, cullTest);
        }
        if (!south && !east) {
            getTemplateBakedModels().get(westLeg).emitQuads(context, cullTest);
        }
    }


    private void middleDesk(BlockRenderView world, BlockState state, BlockPos pos, Random randomSupplier, QuadEmitter context, Predicate<@Nullable Direction> cullTest, boolean left, boolean right, int openOffset) {
        if (left && right) {
            getTemplateBakedModels().get((3 + openOffset)).emitQuads(context, cullTest);
        }  else if (left) {
            getTemplateBakedModels().get((1 + openOffset)).emitQuads(context, cullTest);
        } else if (right) {
            getTemplateBakedModels().get((2 + openOffset)).emitQuads(context, cullTest);
        } else {
            getTemplateBakedModels().get((openOffset)).emitQuads(context, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter emitter, Random randomSupplier) {
        if (blockState == null) return;
        Predicate<Direction> anyPredicate = d -> false;

        pushTextureTransform(emitter, ModelHelper.getOakPlankLogSprites(), getSpriteList(blockState));
        getTemplateBakedModels().get(0).emitQuads(emitter, anyPredicate);
        // legs
        getTemplateBakedModels().get(18).emitQuads(emitter, anyPredicate);

        getTemplateBakedModels().get(16).emitQuads(emitter, anyPredicate);

        getTemplateBakedModels().get(17).emitQuads(emitter, anyPredicate);

        getTemplateBakedModels().get(19).emitQuads(emitter, anyPredicate);
        emitter.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}