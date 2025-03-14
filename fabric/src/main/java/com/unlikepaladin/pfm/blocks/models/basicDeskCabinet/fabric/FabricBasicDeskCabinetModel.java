package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.fabric;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class FabricBasicDeskCabinetModel extends PFMFabricBakedModel {
    public FabricBasicDeskCabinetModel(ModelBakeSettings settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }
    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        if (state.getBlock() instanceof BasicDeskCabinetBlock) {
            BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();
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
                        ((FabricBakedModel) getTemplateBakedModels().get((4 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                    else {
                        ((FabricBakedModel) getTemplateBakedModels().get((5 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, left, right, openOffset);
                }
            }
            else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                // inner corner
                if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && block.isDifferentOrientation(world, pos, neighborFacing)) {
                    if (neighborFacing == isFacing.rotateYCounterclockwise()) {
                        ((FabricBakedModel) getTemplateBakedModels().get((6 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    } else {
                        ((FabricBakedModel) getTemplateBakedModels().get((7 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
                    }
                } else {
                    middleDesk(world, state, pos, randomSupplier, context, left, right, openOffset);
                }
            }
            else {
                middleDesk(world, state, pos, randomSupplier, context, left, right, openOffset);
            }


            boolean north = block.canConnect(world.getBlockState(pos.north()));
            boolean east = block.canConnect(world.getBlockState(pos.east()));
            boolean west = block.canConnect(world.getBlockState(pos.west()));
            boolean south = block.canConnect(world.getBlockState(pos.south()));
            switch (isFacing) {
                case SOUTH:
                        legsDesk(world, state, pos, randomSupplier, context, north, south, east, west, 18, 19, 16, 17);
                        break;
                case NORTH:
                        legsDesk(world, state, pos, randomSupplier, context, north, south, east, west, 17, 16, 19, 18);
                        break;
                case EAST:
                        legsDesk(world, state, pos, randomSupplier, context, north, south, east, west, 19, 17, 18, 16);
                        break;
                default:
                        legsDesk(world, state, pos, randomSupplier, context, north, south, east, west, 16, 18, 17, 19);
                        break;
            }
            context.popTransform();
        }
    }

    private void legsDesk(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        if (!north && !east) {
            ((FabricBakedModel) getTemplateBakedModels().get(northLeg)).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
        if (!north && !west) {
            ((FabricBakedModel) getTemplateBakedModels().get(southLeg)).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
        if (!south && !west) {
            ((FabricBakedModel) getTemplateBakedModels().get(eastLeg)).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
        if (!south && !east) {
            ((FabricBakedModel) getTemplateBakedModels().get(westLeg)).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }


    private void middleDesk(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, boolean left, boolean right, int openOffset) {
        if (left && right) {
            ((FabricBakedModel) getTemplateBakedModels().get((3 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }  else if (left) {
            ((FabricBakedModel) getTemplateBakedModels().get((1 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else if (right) {
            ((FabricBakedModel) getTemplateBakedModels().get((2 + openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        } else {
            ((FabricBakedModel) getTemplateBakedModels().get((openOffset))).emitBlockQuads(world, state, pos, randomSupplier, context);
        }
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(stack));
        ((FabricBakedModel) getTemplateBakedModels().get(0)).emitItemQuads(stack, randomSupplier, context);
        // legs
        ((FabricBakedModel) getTemplateBakedModels().get(18)).emitItemQuads(stack, randomSupplier, context);

        ((FabricBakedModel) getTemplateBakedModels().get(16)).emitItemQuads(stack, randomSupplier, context);

        ((FabricBakedModel) getTemplateBakedModels().get(17)).emitItemQuads(stack, randomSupplier, context);

        ((FabricBakedModel) getTemplateBakedModels().get(19)).emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}