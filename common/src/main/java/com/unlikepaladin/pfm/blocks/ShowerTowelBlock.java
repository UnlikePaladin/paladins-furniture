package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.DinnerTableBlock.rotateShape;

public class ShowerTowelBlock extends HorizontalDirectionalBlock implements DyeableFurnitureBlock {
    private static final List<FurnitureBlock> SHOWER_TOWELS = new ArrayList<>();
    private final DyeColor color;
    public ShowerTowelBlock(DyeColor color, Properties settings) {
        super(settings);
        this.color = color;
        if (this.getClass().isAssignableFrom(ShowerTowelBlock.class)) {
            String towelColor = color.getName();
            SHOWER_TOWELS.add(new FurnitureBlock(this, towelColor+ "_shower_towel"));
        }
    }

    public static Stream<FurnitureBlock> streamShowerTowels() {
        return SHOWER_TOWELS.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public DyeColor getPFMColor() {
        return this.color;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }
    private static final VoxelShape TOWEL_SOUTH = Shapes.or(box(1, 1, 2,15, 13, 5), box(1, 0, 2,15, 1, 3), box(0, 11, 0,1, 12, 4), box(15, 11, 0,16, 12, 4));
    private static final VoxelShape TOWEL_NORTH = rotateShape(Direction.SOUTH, Direction.NORTH, TOWEL_SOUTH);
    private static final VoxelShape TOWEL_EAST = rotateShape(Direction.SOUTH, Direction.EAST, TOWEL_SOUTH);
    private static final VoxelShape TOWEL_WEST = rotateShape(Direction.SOUTH, Direction.WEST, TOWEL_SOUTH);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
       Direction direction = state.getValue(FACING);
       switch (direction) {
           case NORTH: return TOWEL_NORTH;
           case SOUTH: return TOWEL_SOUTH;
           case EAST: return TOWEL_EAST;
           default: return  TOWEL_WEST;
       }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }
}
