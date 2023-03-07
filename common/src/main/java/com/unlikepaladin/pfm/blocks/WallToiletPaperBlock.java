package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class WallToiletPaperBlock extends HorizontalFacingBlock {
    protected static final BooleanProperty WALL = BooleanProperty.of("wall");
    public WallToiletPaperBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(WALL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WALL, FACING);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isSneaking()) {
            world.setBlockState(pos, state.cycle(WALL));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public static final VoxelShape WALL_PAPER_SOUTH = VoxelShapes.union(createCuboidShape(11.5, 12, 12,12.5, 13, 16), createCuboidShape(3.5, 12, 11.5,4.5, 13, 16), createCuboidShape(4.5, 10.5, 10.5,11.5, 14.5, 14.5));
    public static final VoxelShape WALL_PAPER_NORTH = rotateShape(Direction.SOUTH, Direction.NORTH, WALL_PAPER_SOUTH);
    public static final VoxelShape WALL_PAPER_EAST = rotateShape(Direction.SOUTH, Direction.EAST, WALL_PAPER_SOUTH);
    public static final VoxelShape WALL_PAPER_WEST = rotateShape(Direction.SOUTH, Direction.WEST, WALL_PAPER_SOUTH);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case EAST: {
                return WALL_PAPER_EAST;
            }
            case WEST: {
                return WALL_PAPER_WEST;
            }
            case SOUTH: {
                return WALL_PAPER_SOUTH;
            }
            default:
            case NORTH: {
                return WALL_PAPER_NORTH;
            }
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
