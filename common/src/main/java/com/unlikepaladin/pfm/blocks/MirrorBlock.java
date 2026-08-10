package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MirrorBlock extends HorizontalDirectionalBlock {

    protected static List<FurnitureBlock> mirrorBlockList = new ArrayList<>();
    public static final MapCodec<MirrorBlock> CODEC = simpleCodec(MirrorBlock::new);

    public MirrorBlock(BlockBehaviour.Properties settings) {
        super(settings);
        mirrorBlockList.add(new FurnitureBlock(this, "mirror"));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamMirrorBlocks() {
        return mirrorBlockList.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction.getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING) && !state.canSurvive(levelReader, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return  super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    protected static final VoxelShape MIRROR_NORTH = Shapes.or(box(0, 0, 0,16, 16, 1));
    protected static final VoxelShape MIRROR_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, MIRROR_NORTH);

    public boolean canConnect(BlockState neighborState, BlockState state)
    {
        return PaladinFurnitureMod.getPFMConfig().doDifferentMirrorsConnect() ? neighborState.getBlock() instanceof MirrorBlock && neighborState.getValue(FACING) == state.getValue(FACING) : neighborState.getBlock() == state.getBlock() && neighborState.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case SOUTH: {
                return MIRROR_SOUTH;
            }
            case EAST: {
                return MIRROR_EAST;
            }
            case WEST: {
                return MIRROR_WEST;
            }
            default:
            case NORTH: {
                return MIRROR_NORTH;
            }
        }
    }
}