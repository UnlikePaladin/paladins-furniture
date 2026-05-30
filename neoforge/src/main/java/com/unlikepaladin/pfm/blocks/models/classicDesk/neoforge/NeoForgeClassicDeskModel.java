package com.unlikepaladin.pfm.blocks.models.classicDesk.neoforge;

import com.mojang.datafixers.util.Function4;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.neoforge.PFMNeoForgeBakedModel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NeoForgeClassicDeskModel extends PFMNeoForgeBakedModel {
    public NeoForgeClassicDeskModel(ModelState settings, ModelRenderProperties modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void collectParts(BlockAndTintGetter world, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        if (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock) {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            boolean isCabinet = state.getBlock() instanceof ClassicDeskCabinetBlock;
            Function4<BlockGetter, BlockState, BlockPos, BlockPos, Boolean> canConnect = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;
            Function<BlockState, Boolean> canConnectSimple = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;

            boolean north = canConnect.apply(world, state, pos.north(), pos);
            boolean east = canConnect.apply(world, state, pos.east(), pos);
            boolean west = canConnect.apply(world, state, pos.west(), pos);
            boolean south = canConnect.apply(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !canConnect.apply(world, state, pos.north().west(), pos)
                    && (world.getBlockState(pos.north()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.north()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.west()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerNorthEast = north && east && !canConnect.apply(world, state, pos.north().east(), pos)
                    && (world.getBlockState(pos.north()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.north()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.east()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerSouthEast = south && east && !canConnect.apply(world, state, pos.south().east(), pos)
                    && (world.getBlockState(pos.south()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.south()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.east()).getValue(HorizontalDirectionalBlock.FACING) == dir);
            boolean cornerSouthWest = south && west && !canConnect.apply(world, state, pos.south().west(), pos)
                    && (world.getBlockState(pos.south()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.south()).getValue(HorizontalDirectionalBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).hasProperty(HorizontalDirectionalBlock.FACING)
                    && world.getBlockState(pos.west()).getValue(HorizontalDirectionalBlock.FACING) == dir);

            boolean hasCornerNorthWest = north && west && canConnect.apply(world, state, pos.north().west(), pos);
            boolean hasCornerNorthEast = north && east && canConnect.apply(world, state, pos.north().east(), pos);
            boolean hasCornerSouthEast = south && east && canConnect.apply(world, state, pos.south().east(), pos);
            boolean hasCornerSouthWest = south && west && canConnect.apply(world, state, pos.south().west(), pos);
            int openOffset = isCabinet ? state.getValue(ClassicDeskCabinetBlock.OPEN) ? 1 : 0 : 0;

            BlockState rightState = world.getBlockState(pos.relative(dir.getClockWise()));
            boolean right = canConnectSimple.apply(rightState);
            boolean rightCabinet = right && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.relative(dir.getCounterClockWise()));
            boolean left = canConnectSimple.apply(leftState);
            boolean leftCabinet = left && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.relative(dir.getOpposite()));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(dir));

            List<TextureAtlasSprite> spriteList = getSpriteList(state);
            parts.add(getQuadsWithTexture(getTemplateBakedModels().get(0), ModelHelper.getOakPlankLogSprites(), spriteList));

            List<BlockModelPart> preTransformParts = new ArrayList<>();
            boolean wasOuterCorner = false;
            if (isCabinet) {
                if (canConnectSimple.apply(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // inner corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.relative(neighborFacing)))) {
                        if (neighborFacing == dir.getClockWise()) {
                            preTransformParts.add(getTemplateBakedModels().get((34 + openOffset)));
                        }
                        else {
                            preTransformParts.add(getTemplateBakedModels().get((30 + openOffset)));
                        }
                    } else {
                        preTransformParts.add(middleDesk(leftCabinet, rightCabinet, openOffset));
                    }
                }
                else if (canConnectSimple.apply(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // outer corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.relative(neighborFacing.getOpposite())))) {
                        if (neighborFacing == dir.getClockWise()) {
                            preTransformParts.add(getTemplateBakedModels().get((32 + openOffset)));
                        } else {
                            preTransformParts.add(getTemplateBakedModels().get((28 + openOffset)));
                        }
                        wasOuterCorner = true;
                    } else {
                        preTransformParts.add(middleDesk(leftCabinet, rightCabinet, openOffset));
                    }
                }
                else {
                    preTransformParts.add(middleDesk(leftCabinet, rightCabinet, openOffset));
                }
            }

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(1));
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(2));
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(13));
                }
                if (!east && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(12));
                }
                if (!north && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(11));
                }

                // side connecting bits
                if (north && !east && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(7));
                if (south && !east  && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(8));

                if (north && !west && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(9));
                if (south && !west && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(10));

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerNorthWest))
                    preTransformParts.add(getTemplateBakedModels().get(6));
                if (east && !(hasCornerNorthEast))
                    preTransformParts.add(getTemplateBakedModels().get(5));

                // corners
                if (cornerNorthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(15));
                }

                if (cornerNorthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(14));
                }

                if (cornerSouthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(17));
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }

                if (cornerSouthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(16));
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }
            } else if (dir == Direction.SOUTH){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(2));
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(1));
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(12));
                }
                if (!east && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(13));
                }
                if (!south && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(11));
                }

                // side connecting bits
                if (north && !west && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(8));
                if (south && !west && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(7));

                if (north && !east && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(10));
                if (south && !east && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(9));

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerSouthWest))
                    preTransformParts.add(getTemplateBakedModels().get(5));
                if (east && !(hasCornerSouthEast))
                    preTransformParts.add(getTemplateBakedModels().get(6));

                // corners
                if (cornerNorthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(16));
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }

                if (cornerNorthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(17));
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }

                if (cornerSouthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(14));
                }

                if (cornerSouthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(15));
                }
            } else if (dir == Direction.EAST){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(2));
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(1));
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(12));
                }
                if (!north && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(13));
                }
                if (!east && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(11));
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(8));
                if (east && !south && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(7));

                if (west && !north && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(10));
                if (east && !north && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(9));

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthEast))
                    preTransformParts.add(getTemplateBakedModels().get(6));
                if (south && !(hasCornerSouthEast))
                    preTransformParts.add(getTemplateBakedModels().get(5));


                // corners
                if (cornerSouthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(16));
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }

                if (cornerNorthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(17));
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }

                if (cornerSouthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(14));
                }

                if (cornerNorthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(15));
                }
            } else {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(1));
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    preTransformParts.add(getTemplateBakedModels().get(2));
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(13));
                }
                if (!north && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(12));
                }
                if (!west && !wasOuterCorner) {
                    preTransformParts.add(getTemplateBakedModels().get(11));
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(9));
                if (east && !south && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(10));


                if (west && !north && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(7));
                if (east && !north && !wasOuterCorner)
                    preTransformParts.add(getTemplateBakedModels().get(8));

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthWest))
                    preTransformParts.add(getTemplateBakedModels().get(5));
                if (south && !(hasCornerSouthWest))
                    preTransformParts.add(getTemplateBakedModels().get(6));


                // corners
                if (cornerNorthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(16));
                    preTransformParts.add(getTemplateBakedModels().get(3));
                }

                if (cornerSouthEast) {
                    preTransformParts.add(getTemplateBakedModels().get(17));
                    preTransformParts.add(getTemplateBakedModels().get(4));
                }

                if (cornerNorthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(14));
                }

                if (cornerSouthWest) {
                    preTransformParts.add(getTemplateBakedModels().get(15));
                }
            }
            parts.addAll(getTexturedParts(preTransformParts, ModelHelper.getOakPlankLogSprites(), spriteList));
        }
    }

    private BlockModelPart middleDesk(boolean left, boolean right, int openOffset) {
        if (left && right) {
            return ( getTemplateBakedModels().get((22 + openOffset)));
        }  else if (left) {
            return ( getTemplateBakedModels().get((26 + openOffset)));
        } else if (right) {
            return ( getTemplateBakedModels().get((24 + openOffset)));
        } else {
            return ( getTemplateBakedModels().get(20+(openOffset)));
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable Direction face, RandomSource random) {
        if (blockState != null) {
            int offset = blockState.getBlock() instanceof ClassicDeskCabinetBlock ? 1 : 0;
            // base
            return getQuadsWithTextureInner(getTemplateBakedModels().get(18+offset).getQuads(face), ModelHelper.getOakPlankLogSprites(), getSpriteList(blockState));
        }
        return Collections.emptyList();
    }
}