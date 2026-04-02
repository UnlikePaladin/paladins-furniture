package com.unlikepaladin.pfm.blocks.models.classicDesk.forge;

import com.mojang.datafixers.util.Function4;
import com.unlikepaladin.pfm.blocks.ClassicDeskBlock;
import com.unlikepaladin.pfm.blocks.ClassicDeskCabinetBlock;
import com.unlikepaladin.pfm.blocks.models.ModelHelper;
import com.unlikepaladin.pfm.blocks.models.forge.PFMForgeBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;

import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeClassicDeskModel extends PFMForgeBakedModel {
    public ForgeClassicDeskModel(ModelState settings, List<BakedModel> modelParts) {
        super(settings, modelParts);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData tileData) {
        if (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock) {
            ModelData.Builder builder = ModelData.builder();

            ModelData data = builder.build();
            data = super.getModelData(world, pos, state, data);

            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
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
            BlockState rightState = world.getBlockState(pos.relative(dir.getClockWise()));
            boolean right = canConnectSimple.apply(rightState);
            boolean rightCabinet = right && rightState.getBlock() instanceof ClassicDeskCabinetBlock;

            BlockState leftState = world.getBlockState(pos.relative(dir.getCounterClockWise()));
            boolean left = canConnectSimple.apply(leftState);
            boolean leftCabinet = left && leftState.getBlock() instanceof ClassicDeskCabinetBlock;


            BlockState neighborStateFacing = world.getBlockState(pos.relative(dir.getOpposite()));
            BlockState neighborStateOpposite = world.getBlockState(pos.relative(dir));
            BlockState neighborStateFacingNeighbor = neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ?
                    world.getBlockState(pos.relative(neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING))) : Blocks.AIR.defaultBlockState();
            BlockState neigborStateOppositeNeigbor = neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING) ?
                    world.getBlockState(pos.relative(neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite())) : Blocks.AIR.defaultBlockState();

            DeskModelData deskModelData = new DeskModelData(north, south,  east, west, cornerNorthWest, cornerNorthEast,
                    cornerSouthEast, cornerSouthWest, hasCornerNorthWest, hasCornerNorthEast,
                    hasCornerSouthEast, hasCornerSouthWest, left, right, leftCabinet, rightCabinet,
                    rightState, leftState, neighborStateFacing, neighborStateOpposite, neighborStateFacingNeighbor, neigborStateOppositeNeigbor, dir);
            data = data.derive().with(DESK_DATA, deskModelData).build();
            return data;
        }
        return tileData;
    }

    public static ModelProperty<DeskModelData> DESK_DATA = new ModelProperty<>();

    public static class DeskModelData {
        public final boolean north, south, east, west, cornerNorthWest, cornerNorthEast,
                cornerSouthEast, cornerSouthWest, hasCornerNorthWest, hasCornerNorthEast,
                hasCornerSouthEast, hasCornerSouthWest, left, right, leftCabinet, rightCabinet;
        public final BlockState rightState, leftState, neighborStateFacing, neighborStateOpposite,
                neighborStateFacingNeighbor, neigborStateOppositeNeigbor;
        public final Direction facing;


        DeskModelData(boolean north, boolean south, boolean east, boolean west, boolean cornerNorthWest, boolean cornerNorthEast, boolean cornerSouthEast, boolean cornerSouthWest, boolean hasCornerNorthWest, boolean hasCornerNorthEast, boolean hasCornerSouthEast, boolean hasCornerSouthWest, boolean left, boolean right, boolean leftCabinet, boolean rightCabinet, BlockState rightState, BlockState leftState, BlockState neighborStateFacing, BlockState neighborStateOpposite, BlockState neighborStateFacingNeighbor, BlockState neigborStateOppositeNeigbor, Direction facing) {
            this.north = north;
            this.south = south;
            this.east = east;
            this.west = west;
            this.cornerNorthWest = cornerNorthWest;
            this.cornerNorthEast = cornerNorthEast;
            this.cornerSouthEast = cornerSouthEast;
            this.cornerSouthWest = cornerSouthWest;
            this.hasCornerNorthWest = hasCornerNorthWest;
            this.hasCornerNorthEast = hasCornerNorthEast;
            this.hasCornerSouthEast = hasCornerSouthEast;
            this.hasCornerSouthWest = hasCornerSouthWest;
            this.left = left;
            this.right = right;
            this.leftCabinet = leftCabinet;
            this.rightCabinet = rightCabinet;
            this.rightState = rightState;
            this.leftState = leftState;
            this.neighborStateFacing = neighborStateFacing;
            this.neighborStateOpposite = neighborStateOpposite;
            this.neighborStateFacingNeighbor = neighborStateFacingNeighbor;
            this.neigborStateOppositeNeigbor = neigborStateOppositeNeigbor;
            this.facing = facing;
        }
    }


    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType renderLayer) {
        DeskModelData deskData = extraData.get(DESK_DATA);
        if (deskData != null && (state.getBlock() instanceof ClassicDeskBlock || state.getBlock() instanceof ClassicDeskCabinetBlock)) {

            boolean isCabinet = state.getBlock() instanceof ClassicDeskCabinetBlock;
            int openOffset = isCabinet ? state.getValue(ClassicDeskCabinetBlock.OPEN) ? 1 : 0 : 0;

            boolean north = deskData.north;
            boolean east = deskData.east;
            boolean west = deskData.west;
            boolean south = deskData.south;
            boolean cornerNorthWest = deskData.cornerNorthWest;
            boolean cornerNorthEast = deskData.cornerNorthEast;
            boolean cornerSouthEast = deskData.cornerSouthEast;
            boolean cornerSouthWest = deskData.cornerSouthWest;
            boolean hasCornerNorthWest = deskData.hasCornerNorthWest;
            boolean hasCornerNorthEast = deskData.hasCornerNorthEast;
            boolean hasCornerSouthEast = deskData.hasCornerSouthEast;
            boolean hasCornerSouthWest = deskData.hasCornerSouthWest;
            boolean left = deskData.left;
            boolean right = deskData.right;
            boolean leftCabinet = deskData.leftCabinet;
            boolean rightCabinet = deskData.rightCabinet;
            BlockState neighborStateFacing = deskData.neighborStateFacing;
            BlockState neighborStateOpposite = deskData.neighborStateOpposite;
            Direction dir = deskData.facing;
            Function<BlockState, Boolean> canConnectSimple = state.getBlock() instanceof ClassicDeskBlock desk ? desk::canConnect : ((ClassicDeskCabinetBlock) state.getBlock())::canConnect;
            BlockState neighborStateFacingNeighbor = deskData.neighborStateFacingNeighbor;
            BlockState neigborStateOppositeNeigbor = deskData.neigborStateOppositeNeigbor;

            List<BakedQuad> blockQuads = new ArrayList<>(getTemplateBakedModels().get(0).getQuads(state, side, rand, extraData, renderLayer));

            boolean wasOuterCorner = false;
            if (isCabinet) {
                if (canConnectSimple.apply(neighborStateFacing) && neighborStateFacing.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateFacing.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // inner corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(neighborStateFacingNeighbor)) {
                        if (neighborFacing == dir.getClockWise()) {
                            blockQuads.addAll(getTemplateBakedModels().get((34 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                        }
                        else {
                            blockQuads.addAll(getTemplateBakedModels().get((30 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                        }
                    } else {
                        blockQuads.addAll(middleDesk(state, side, rand, renderLayer, extraData, leftCabinet, rightCabinet, openOffset));
                    }
                }
                else if (canConnectSimple.apply(neighborStateOpposite) && neighborStateOpposite.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                    Direction neighborFacing = neighborStateOpposite.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    // outer corner
                    if (neighborFacing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() && !canConnectSimple.apply(neigborStateOppositeNeigbor)) {
                        if (neighborFacing == dir.getClockWise()) {
                            blockQuads.addAll(getTemplateBakedModels().get((32 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                        } else {
                            blockQuads.addAll(getTemplateBakedModels().get((28 + openOffset)).getQuads(state, side, rand, extraData, renderLayer));
                        }
                        wasOuterCorner = true;
                    } else {
                        blockQuads.addAll(middleDesk(state, side, rand, renderLayer, extraData, leftCabinet, rightCabinet, openOffset));
                    }
                }
                else {
                    blockQuads.addAll(middleDesk(state, side, rand, renderLayer, extraData, leftCabinet, rightCabinet, openOffset));
                }
            }

            if (dir == Direction.NORTH) {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(13).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!east && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(12).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!north && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(11).getQuads(state, side, rand, extraData, renderLayer));
                }

                // side connecting bits
                if (north && !east && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(7).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !east  && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(8).getQuads(state, side, rand, extraData, renderLayer));

                if (north && !west && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(9).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !west && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(10).getQuads(state, side, rand, extraData, renderLayer));

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerNorthWest))
                    blockQuads.addAll(getTemplateBakedModels().get(6).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !(hasCornerNorthEast))
                    blockQuads.addAll(getTemplateBakedModels().get(5).getQuads(state, side, rand, extraData, renderLayer));

                // corners
                if (cornerNorthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(15).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerNorthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(14).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(17).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(16).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }
            } else if (dir == Direction.SOUTH){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                   blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData, renderLayer));
                }

                // east & west & north bits
                if (!west && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(12).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!east && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(13).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(11).getQuads(state, side, rand, extraData, renderLayer));
                }

                // side connecting bits
                if (north && !west && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(8).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !west && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(7).getQuads(state, side, rand, extraData, renderLayer));

                if (north && !east && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(10).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !east && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(9).getQuads(state, side, rand, extraData, renderLayer));

                // the connection up front, this here to avoid it when centered
                if (west && !(hasCornerSouthWest))
                    blockQuads.addAll(getTemplateBakedModels().get(5).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !(hasCornerSouthEast))
                  blockQuads.addAll(getTemplateBakedModels().get(6).getQuads(state, side, rand, extraData, renderLayer));

                // corners
                if (cornerNorthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(16).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerNorthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(17).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(14).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(15).getQuads(state, side, rand, extraData, renderLayer));
                }
            } else if (dir == Direction.EAST){
                // le legs
                if (!east && !north || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!west && !north || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !east || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !west || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(12).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!north && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(13).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!east && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(11).getQuads(state, side, rand, extraData, renderLayer));
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(8).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !south && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(7).getQuads(state, side, rand, extraData, renderLayer));

                if (west && !north && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(10).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !north && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(9).getQuads(state, side, rand, extraData, renderLayer));

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthEast))
                    blockQuads.addAll(getTemplateBakedModels().get(6).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !(hasCornerSouthEast))
                    blockQuads.addAll(getTemplateBakedModels().get(5).getQuads(state, side, rand, extraData, renderLayer));


                // corners
                if (cornerSouthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(16).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerNorthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(17).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(14).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerNorthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(15).getQuads(state, side, rand, extraData, renderLayer));
                }
            } else {
                // le legs
                if (!east && !north || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!west && !north || (isCabinet && right && !rightCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(1).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !east || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!south && !west || (isCabinet && left && !leftCabinet))  {
                    blockQuads.addAll(getTemplateBakedModels().get(2).getQuads(state, side, rand, extraData, renderLayer));
                }

                // east & west & north bits
                if (!south && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(13).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!north && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(12).getQuads(state, side, rand, extraData, renderLayer));
                }
                if (!west && !wasOuterCorner) {
                    blockQuads.addAll(getTemplateBakedModels().get(11).getQuads(state, side, rand, extraData, renderLayer));
                }

                // side connecting bits
                if (west && !south && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(9).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !south && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(10).getQuads(state, side, rand, extraData, renderLayer));


                if (west && !north && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(7).getQuads(state, side, rand, extraData, renderLayer));
                if (east && !north && !wasOuterCorner)
                    blockQuads.addAll(getTemplateBakedModels().get(8).getQuads(state, side, rand, extraData, renderLayer));

                // the connection up front, this here to avoid it when centered
                if (north && !(hasCornerNorthWest))
                    blockQuads.addAll(getTemplateBakedModels().get(5).getQuads(state, side, rand, extraData, renderLayer));
                if (south && !(hasCornerSouthWest))
                    blockQuads.addAll(getTemplateBakedModels().get(6).getQuads(state, side, rand, extraData, renderLayer));


                // corners
                if (cornerNorthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(16).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(3).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthEast) {
                    blockQuads.addAll(getTemplateBakedModels().get(17).getQuads(state, side, rand, extraData, renderLayer));
                    blockQuads.addAll(getTemplateBakedModels().get(4).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerNorthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(14).getQuads(state, side, rand, extraData, renderLayer));
                }

                if (cornerSouthWest) {
                    blockQuads.addAll(getTemplateBakedModels().get(15).getQuads(state, side, rand, extraData, renderLayer));
                }
            }
            return getQuadsWithTexture(blockQuads, ModelHelper.getOakPlankLogSprites(), getSpriteList(state));
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> middleDesk(BlockState state, Direction side, RandomSource rand, RenderType renderLayer, ModelData extraData, boolean left, boolean right, int openOffset) {
        if (left && right) {
            return getTemplateBakedModels().get((22 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        }  else if (left) {
            return getTemplateBakedModels().get((26 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        } else if (right) {
            return getTemplateBakedModels().get((24 + openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        } else {
            return getTemplateBakedModels().get(20+(openOffset)).getQuads(state, side, rand, extraData, renderLayer);
        }
    }

    @Override
    public List<BakedQuad> getQuads(ItemStack stack, @Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        if (stack.getItem() instanceof BlockItem) {
            int offset = ((BlockItem) stack.getItem()).getBlock() instanceof ClassicDeskCabinetBlock ? 1 : 0;
            // base
            return getQuadsWithTexture(getTemplateBakedModels().get(18+offset).getQuads(state, face, random), ModelHelper.getOakPlankLogSprites(), getSpriteList(stack));
        }
        return super.getQuads(stack, state, face, random);
    }

    @Override
    public ItemTransforms getTransforms() {
        return getTemplateBakedModels().get(19).getTransforms();
    }
}