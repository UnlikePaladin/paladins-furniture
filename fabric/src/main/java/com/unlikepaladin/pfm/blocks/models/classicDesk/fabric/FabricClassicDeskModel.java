package com.unlikepaladin.pfm.blocks.models.classicDesk.fabric;

import com.mojang.datafixers.util.Function4;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.fabric.PFMFabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelSettings;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricClassicDeskModel extends PFMFabricBakedModel {
    public FabricClassicDeskModel(ModelBakeSettings settings, ModelSettings modelSettings, List<BlockModelPart> modelParts) {
        super(settings, modelSettings, modelParts);
    }

    @Override
    public void emitQuads(QuadEmitter context, BlockRenderView world, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        if (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock) {
            Direction dir = state.get(HorizontalFacingBlock.FACING);
            boolean isCabinet = state.getBlock() instanceof ClassicDeskCabinetBlock;
            Function4<BlockView, BlockState, BlockPos, BlockPos, Boolean> canConnect = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;
            Function<BlockState, Boolean> canConnectSimple = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;

            boolean north = canConnect.apply(world, state, pos.north(), pos);
            boolean east = canConnect.apply(world, state, pos.east(), pos);
            boolean west = canConnect.apply(world, state, pos.west(), pos);
            boolean south = canConnect.apply(world, state, pos.south(), pos);
            boolean cornerNorthWest = north && west && !canConnect.apply(world, state, pos.north().west(), pos)
                    && (world.getBlockState(pos.north()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.north()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.west()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerNorthEast = north && east && !canConnect.apply(world, state, pos.north().east(), pos)
                    && (world.getBlockState(pos.north()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.north()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.east()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerSouthEast = south && east && !canConnect.apply(world, state, pos.south().east(), pos)
                    && (world.getBlockState(pos.south()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.south()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.east()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.east()).get(HorizontalFacingBlock.FACING) == dir);
            boolean cornerSouthWest = south && west && !canConnect.apply(world, state, pos.south().west(), pos)
                    && (world.getBlockState(pos.south()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.south()).get(HorizontalFacingBlock.FACING) == dir)
                    && (world.getBlockState(pos.west()).contains(HorizontalFacingBlock.FACING)
                    && world.getBlockState(pos.west()).get(HorizontalFacingBlock.FACING) == dir);

            boolean hasCornerNorthWest = north && west && canConnect.apply(world, state, pos.north().west(), pos);
            boolean hasCornerNorthEast = north && east && canConnect.apply(world, state, pos.north().east(), pos);
            boolean hasCornerSouthEast = south && east && canConnect.apply(world, state, pos.south().east(), pos);
            boolean hasCornerSouthWest = south && west && canConnect.apply(world, state, pos.south().west(), pos);
            int openOffset = isCabinet ? state.get(ClassicDeskCabinetBlock.OPEN) ? 1 : 0 : 0;


            BlockState rightState = world.getBlockState(pos.offset(dir.rotateYClockwise()));
            boolean right = canConnectSimple.apply(rightState);
            boolean rightCabinet = right && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.offset(dir.rotateYCounterclockwise()));
            boolean left = canConnectSimple.apply(leftState);
            boolean leftCabinet = left && leftState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState neighborStateFacing = world.getBlockState(pos.offset(dir.getOpposite()));
            BlockState neighborStateOpposite = world.getBlockState(pos.offset(dir));

            List<Sprite> spriteList = getSpriteList(state);
            pushTextureTransform(context, spriteList.get(0));
            (getTemplateBakedModels().get(0)).emitQuads(context, cullTest);;
            context.popTransform();


            pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));

            boolean wasOuterCorner = false;
            if (isCabinet) {
                if (canConnectSimple.apply(neighborStateFacing) && neighborStateFacing.contains(Properties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateFacing.get(Properties.HORIZONTAL_FACING);
                    // inner corner
                    if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.offset(neighborFacing)))) {
                        if (neighborFacing == dir.rotateYClockwise()) {
                            ( getTemplateBakedModels().get((34 + openOffset))).emitQuads(context, cullTest);
                        }
                        else {
                            ( getTemplateBakedModels().get((30 + openOffset))).emitQuads(context, cullTest);
                        }
                    } else {
                        middleDesk(context, leftCabinet, rightCabinet, openOffset, cullTest);
                    }
                }
                else if (canConnectSimple.apply(neighborStateOpposite) && neighborStateOpposite.contains(Properties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateOpposite.get(Properties.HORIZONTAL_FACING);
                    // outer corner
                    if (neighborFacing.getAxis() != state.get(Properties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(world.getBlockState(pos.offset(neighborFacing.getOpposite())))) {
                        if (neighborFacing == dir.rotateYClockwise()) {
                            ( getTemplateBakedModels().get((32 + openOffset))).emitQuads(context, cullTest);
                        } else {
                            ( getTemplateBakedModels().get((28 + openOffset))).emitQuads(context, cullTest);
                        }
                        wasOuterCorner = true;
                    } else {
                        middleDesk(context, leftCabinet, rightCabinet, openOffset, cullTest);
                    }
                }
                else {
                    middleDesk(context, leftCabinet, rightCabinet, openOffset, cullTest);
                }
            }

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(1)).emitQuads(context, cullTest);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(2)).emitQuads(context, cullTest);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(13)).emitQuads(context, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(12)).emitQuads(context, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(11)).emitQuads(context, cullTest);
                }

                // side connecting bits
                if (north && !east && !wasOuterCorner)
                    ( getTemplateBakedModels().get(7)).emitQuads(context, cullTest);
                if (south && !east  && !wasOuterCorner)
                    ( getTemplateBakedModels().get(8)).emitQuads(context, cullTest);

                if (north && !west && !wasOuterCorner)
                    ( getTemplateBakedModels().get(9)).emitQuads(context, cullTest);
                if (south && !west && !wasOuterCorner)
                    ( getTemplateBakedModels().get(10)).emitQuads(context, cullTest);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerNorthWest))
                    ( getTemplateBakedModels().get(6)).emitQuads(context, cullTest);
                if (east && !(hasCornerNorthEast))
                    ( getTemplateBakedModels().get(5)).emitQuads(context, cullTest);

                // corners
                if (cornerNorthWest) {
                    ( getTemplateBakedModels().get(15)).emitQuads(context, cullTest);
                }

                if (cornerNorthEast) {
                    ( getTemplateBakedModels().get(14)).emitQuads(context, cullTest);
                }

                if (cornerSouthWest) {
                    ( getTemplateBakedModels().get(17)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }

                if (cornerSouthEast) {
                    ( getTemplateBakedModels().get(16)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }
            } else if (dir == Direction.SOUTH){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                   ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(2)).emitQuads(context, cullTest);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(1)).emitQuads(context, cullTest);
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(12)).emitQuads(context, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(13)).emitQuads(context, cullTest);
                }
                if (!south && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(11)).emitQuads(context, cullTest);
                }

                // side connecting bits
                if (north && !west && !wasOuterCorner)
                    ( getTemplateBakedModels().get(8)).emitQuads(context, cullTest);
                if (south && !west && !wasOuterCorner)
                    ( getTemplateBakedModels().get(7)).emitQuads(context, cullTest);

                if (north && !east && !wasOuterCorner)
                    ( getTemplateBakedModels().get(10)).emitQuads(context, cullTest);
                if (south && !east && !wasOuterCorner)
                    ( getTemplateBakedModels().get(9)).emitQuads(context, cullTest);

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerSouthWest))
                    ( getTemplateBakedModels().get(5)).emitQuads(context, cullTest);
                if (east && !(hasCornerSouthEast))
                  ( getTemplateBakedModels().get(6)).emitQuads(context, cullTest);

                // corners
                if (cornerNorthWest) {
                    ( getTemplateBakedModels().get(16)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }

                if (cornerNorthEast) {
                    ( getTemplateBakedModels().get(17)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }

                if (cornerSouthWest) {
                    ( getTemplateBakedModels().get(14)).emitQuads(context, cullTest);
                }

                if (cornerSouthEast) {
                    ( getTemplateBakedModels().get(15)).emitQuads(context, cullTest);
                }
            } else if (dir == Direction.EAST){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(2)).emitQuads(context, cullTest);
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(1)).emitQuads(context, cullTest);
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(12)).emitQuads(context, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(13)).emitQuads(context, cullTest);
                }
                if (!east && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(11)).emitQuads(context, cullTest);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ( getTemplateBakedModels().get(8)).emitQuads(context, cullTest);
                if (east && !south && !wasOuterCorner)
                    ( getTemplateBakedModels().get(7)).emitQuads(context, cullTest);

                if (west && !north && !wasOuterCorner)
                    ( getTemplateBakedModels().get(10)).emitQuads(context, cullTest);
                if (east && !north && !wasOuterCorner)
                    ( getTemplateBakedModels().get(9)).emitQuads(context, cullTest);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthEast))
                    ( getTemplateBakedModels().get(6)).emitQuads(context, cullTest);
                if (south && !(hasCornerSouthEast))
                    ( getTemplateBakedModels().get(5)).emitQuads(context, cullTest);


                // corners
                if (cornerSouthWest) {
                    ( getTemplateBakedModels().get(16)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }

                if (cornerNorthWest) {
                    ( getTemplateBakedModels().get(17)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }

                if (cornerSouthEast) {
                    ( getTemplateBakedModels().get(14)).emitQuads(context, cullTest);
                }

                if (cornerNorthEast) {
                    ( getTemplateBakedModels().get(15)).emitQuads(context, cullTest);
                }
            } else {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    ( getTemplateBakedModels().get(1)).emitQuads(context, cullTest);
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    ( getTemplateBakedModels().get(2)).emitQuads(context, cullTest);
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(13)).emitQuads(context, cullTest);
                }
                if (!north && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(12)).emitQuads(context, cullTest);
                }
                if (!west && !wasOuterCorner) {
                    ( getTemplateBakedModels().get(11)).emitQuads(context, cullTest);
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    ( getTemplateBakedModels().get(9)).emitQuads(context, cullTest);
                if (east && !south && !wasOuterCorner)
                    ( getTemplateBakedModels().get(10)).emitQuads(context, cullTest);


                if (west && !north && !wasOuterCorner)
                    ( getTemplateBakedModels().get(7)).emitQuads(context, cullTest);
                if (east && !north && !wasOuterCorner)
                    ( getTemplateBakedModels().get(8)).emitQuads(context, cullTest);

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthWest))
                    ( getTemplateBakedModels().get(5)).emitQuads(context, cullTest);
                if (south && !(hasCornerSouthWest))
                    ( getTemplateBakedModels().get(6)).emitQuads(context, cullTest);


                // corners
                if (cornerNorthEast) {
                    ( getTemplateBakedModels().get(16)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(3)).emitQuads(context, cullTest);
                }

                if (cornerSouthEast) {
                    ( getTemplateBakedModels().get(17)).emitQuads(context, cullTest);
                    ( getTemplateBakedModels().get(4)).emitQuads(context, cullTest);
                }

                if (cornerNorthWest) {
                    ( getTemplateBakedModels().get(14)).emitQuads(context, cullTest);
                }

                if (cornerSouthWest) {
                    ( getTemplateBakedModels().get(15)).emitQuads(context, cullTest);
                }
            }
            context.popTransform();
        }
    }

    private void middleDesk(QuadEmitter context, boolean left, boolean right, int openOffset, Predicate<@Nullable  Direction> cullTest) {
        if (left && right) {
            ( getTemplateBakedModels().get((22 + openOffset))).emitQuads(context, cullTest);
        }  else if (left) {
            ( getTemplateBakedModels().get((26 + openOffset))).emitQuads(context, cullTest);
        } else if (right) {
            ( getTemplateBakedModels().get((24 + openOffset))).emitQuads(context, cullTest);
        } else {
            ( getTemplateBakedModels().get(20+(openOffset))).emitQuads(context, cullTest);
        }
    }

    @Override
    public void emitItemQuads(QuadEmitter context, Random random) {
        if (blockState == null) return;

        int offset = blockState.getBlock() instanceof ClassicDeskCabinetBlock ? 1 : 0;
        pushTextureTransform(context, ModelHelper.getOakPlankLogSprites(), getSpriteList(blockState));
        // base
        Predicate<Direction> anyPredicate = d -> false;
        ( getTemplateBakedModels().get(18+offset)).emitQuads(context, anyPredicate);
        context.popTransform();
    }

    @Override
    public Sprite pfm$getParticle(BlockState state) {
        return getSpriteList(state).get(0);
    }
}