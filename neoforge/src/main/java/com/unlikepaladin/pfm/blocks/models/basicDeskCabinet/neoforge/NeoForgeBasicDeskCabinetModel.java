package com.unlikepaladin.pfm.blocks.models.basicDeskCabinet.neoforge;

import com.unlikepaladin.pfm.blocks.BasicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeBasicDeskCabinetModel extends PFMNeoForgeBakedModel {
    public NeoForgeBasicDeskCabinetModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state == null || !(state.getBlock() instanceof BasicDeskCabinetBlock))
            return;

        BasicDeskCabinetBlock block = (BasicDeskCabinetBlock) state.getBlock();
        boolean north = block.canConnect(world.getBlockState(pos.north()));
        boolean east = block.canConnect(world.getBlockState(pos.east()));
        boolean west = block.canConnect(world.getBlockState(pos.west()));
        boolean south = block.canConnect(world.getBlockState(pos.south()));

        Direction isFacing = state.getValue(BasicDeskCabinetBlock.FACING);
        BlockState neighborStateFacing = world.getBlockState(pos.relative(isFacing));
        BlockState neighborStateOpposite = world.getBlockState(pos.relative(isFacing.getOpposite()));

        boolean isNeighborStateOppositeFacingDifferentDirection;
        if (neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            isNeighborStateOppositeFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING));
        } else {
            isNeighborStateOppositeFacingDifferentDirection = false;
        }

        boolean isNeighborStateFacingDifferentDirection;
        if (neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            isNeighborStateFacingDifferentDirection = block.isDifferentOrientation(world, pos, neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
        } else {
            isNeighborStateFacingDifferentDirection = false;
        }

        BlockState rightState = world.getBlockState(pos.relative(isFacing.getCounterClockWise()));
        boolean right = block.canConnect(rightState) && rightState.getBlock() instanceof BasicDeskCabinetBlock;

        BlockState leftState = world.getBlockState(pos.relative(isFacing.getClockWise()));
        boolean left = block.canConnect(leftState) && leftState.getBlock() instanceof BasicDeskCabinetBlock;

        List<BlockModelPart> secondaryQuads = new ArrayList<>();
        switch (isFacing) {
            case SOUTH:
                secondaryQuads.addAll(legsDesk(north, south, east, west, 18, 19, 16, 17));
                break;
            case NORTH:
                secondaryQuads.addAll(legsDesk(north, south, east, west, 17, 16, 19, 18));
                break;
            case EAST:
                secondaryQuads.addAll(legsDesk(north, south, east, west, 19, 17, 18, 16));
                break;
            default:
                secondaryQuads.addAll(legsDesk(north, south, east, west, 16, 18, 17, 19));
                break;
        }

        int openOffset = state.getValue(BasicDeskCabinetBlock.OPEN) ? 8 : 0;

        if (block.canConnect(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
            // outer corner
            if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateFacingDifferentDirection) {
                if (neighborFacing == isFacing.getCounterClockWise()) {
                    secondaryQuads.add(getTemplateBakedModels().get((4 + openOffset)));
                }
                else {
                    secondaryQuads.add(getTemplateBakedModels().get((5 + openOffset)));
                }
            } else {
                secondaryQuads.add(middleDesk(left, right, openOffset));
            }
        }
        else if (block.canConnect(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
            // inner corner
            if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && isNeighborStateOppositeFacingDifferentDirection) {
                if (neighborFacing == isFacing.getCounterClockWise()) {
                    secondaryQuads.add(getTemplateBakedModels().get((6 + openOffset)));
                } else {
                    secondaryQuads.add(getTemplateBakedModels().get((7 + openOffset)));
                }
            } else {
                secondaryQuads.add(middleDesk(left, right, openOffset));
            }
        }
        else {
            secondaryQuads.add(middleDesk(left, right, openOffset));
        }

        List<TextureAtlasSprite> spriteList = getSpriteList(state);
        parts.addAll(getTexturedParts(secondaryQuads, ModelHelper.getOakPlankLogSprites(), spriteList));
    }

    private List<BlockModelPart> legsDesk(boolean north, boolean south, boolean west, boolean east, int northLeg, int southLeg, int westLeg, int eastLeg) {
        List<BlockModelPart> modelParts = new ArrayList<>();
        if (!north && !east) {
            modelParts.add(getTemplateBakedModels().get(northLeg));
        }
        if (!north && !west) {
            modelParts.add(getTemplateBakedModels().get(southLeg));
        }
        if (!south && !west) {
            modelParts.add(getTemplateBakedModels().get(eastLeg));
        }
        if (!south && !east) {
            modelParts.add(getTemplateBakedModels().get(westLeg));
        }
        return modelParts;
    }


    private BlockModelPart middleDesk(boolean left, boolean right, int openOffset) {
        if (left && right) {
            return getTemplateBakedModels().get((3 + openOffset));
        }  else if (left) {
            return getTemplateBakedModels().get((1 + openOffset));
        } else if (right) {
            return getTemplateBakedModels().get((2 + openOffset));
        } else {
            return getTemplateBakedModels().get((openOffset));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        // base
        List<BakedQuad> baseQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(face));

        List<BakedQuad> secondaryQuads = new ArrayList<>();
        // legs
        secondaryQuads.addAll(getTemplateBakedModels().get(16).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(18).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(17).getQuads(face));
        secondaryQuads.addAll(getTemplateBakedModels().get(19).getQuads(face));
        // in between pieces

        List<TextureAtlasSprite> spriteList = getSpriteList(blockState);
        List<BakedQuad> quads = getQuadsWithTexture(baseQuads, new SpriteData(spriteList.get(0)));
        quads.addAll(getQuadsWithTexture(secondaryQuads, new SpriteData(spriteList.get(1))));
        return quads;
    }
}