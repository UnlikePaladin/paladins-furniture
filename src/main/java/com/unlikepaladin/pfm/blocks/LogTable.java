package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogTable extends HorizontalFacingBlock {

    private final Block baseBlock;
    public static final EnumProperty<TableShape> SHAPE = EnumProperty.of("table_type", TableShape.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;


    private final BlockState baseBlockState;
    private static final List<LogTable> WOOD_LOG_TABLES = new ArrayList<>();
    public LogTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(SHAPE, TableShape.SINGLE).with(FACING, Direction.NORTH));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(LogTable.class)){
            WOOD_LOG_TABLES.add(this);
        }
    }

    public static Stream<LogTable> streamWoodLogTables() {
        return WOOD_LOG_TABLES.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
        stateManager.add(FACING);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return (BlockState) getShape(blockState, ctx.getWorld(), ctx.getBlockPos(), blockState.get(FACING));
    }



    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? getShape(state, world, pos, state.get(FACING)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private boolean isTable(WorldAccess world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        if(state.getBlock() == this)
        {
            Direction sourceDirection = state.get(FACING);
            return sourceDirection.equals(tableDirection);
        }
        return false;
    }

    public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos, Direction dir)
    {
        boolean left = isTable(world, pos, dir.rotateYCounterclockwise(), dir);
        boolean right = isTable(world, pos, dir.rotateYClockwise(), dir);
        if(left && right)
        {
            return state.with(SHAPE, TableShape.MIDDLE);
        }
        else if(left)
        {
            return state.with(SHAPE, TableShape.RIGHT);
        }
        else if(right)
        {
            return state.with(SHAPE, TableShape.LEFT);
        }
        return state.with(SHAPE, TableShape.SINGLE);
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    /**
     * Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }


    protected TableShape getShape(BlockState state) {
        return state.get(SHAPE);
    }

    final static VoxelShape log_table = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 5, 4.5, 14, 11), createCuboidShape(11.5, 0, 5, 14, 14, 11));
    final static VoxelShape log_table_middle = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16));
    final static VoxelShape log_table_one = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(6, 0, 5, 8.5, 14, 11));
    final static VoxelShape log_table_one_west = rotateShape(Direction.NORTH, Direction.WEST, log_table_one);
    final static VoxelShape log_table_one_south = rotateShape(Direction.NORTH, Direction.SOUTH, log_table_one);
    final static VoxelShape log_table_one_east = rotateShape(Direction.NORTH, Direction.EAST, log_table_one);
    final static VoxelShape log_table_east = rotateShape(Direction.NORTH, Direction.EAST, log_table);
    //Cursed I know
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        TableShape tableShape = getShape(state);
        Direction dir = state.get(FACING);
        boolean dirNorthOrSouth = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
        boolean dirWestOrEast = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);

        switch (tableShape) {
            case LEFT -> {
                if (dirNorthOrSouth) {
                    return log_table_one;}
                else if (dirWestOrEast) {
                    return log_table_one_west;}
                else {
                    return log_table;
                }
            }
            case RIGHT -> {
                if (dirNorthOrSouth) {
                return log_table_one_south;}
                else if (dirWestOrEast) {
                    return log_table_one_east;}
                else {
                    return log_table;
                }
            }
            case MIDDLE -> {
                return log_table_middle;
            }
            default -> {
                if (dirWestOrEast) {
                    return log_table_east;}
                else {
                    return log_table;
                }
            }
        }

    }
}


