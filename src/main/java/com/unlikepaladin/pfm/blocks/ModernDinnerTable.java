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

public class ModernDinnerTable extends HorizontalFacingBlock {

    private final Block baseBlock;
    public static final EnumProperty<TableShape> SHAPE = EnumProperty.of("table_type", TableShape.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;


    private final BlockState baseBlockState;

    public ModernDinnerTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(SHAPE, TableShape.SINGLE).with(FACING, Direction.NORTH));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
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
        return state.get(this.SHAPE);
    }

    final static VoxelShape modern_dinner_table = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(13, 2, 7,15, 14, 9), createCuboidShape(1, 2, 7, 3, 14, 9),createCuboidShape(2, 0, 2,4, 14, 4),createCuboidShape(2, 0, 4, 4, 2, 12), createCuboidShape(3, 2, 7,13, 4, 9),createCuboidShape(12, 0, 4,14, 2, 12), createCuboidShape(2, 0, 12,4, 14, 14));
    final static VoxelShape modern_dinner_table_middle = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16),createCuboidShape(0, 2, 7,16, 4, 9 ));
    final static VoxelShape modern_dinner_table_one = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 2, 7, 15, 14, 9), createCuboidShape(12, 0, 12,14, 14, 14), createCuboidShape(12, 0, 4,14, 2, 12 ), createCuboidShape(0, 2, 7,13, 4, 9), createCuboidShape(12, 0, 2,14, 14, 4 ));

    //Cursed I know
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        TableShape tableShape = getShape(state);
        Direction dir = state.get(FACING);
        boolean dirNorthOrSouth = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
        boolean dirWestOrEast = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);

        switch (tableShape) {
            case LEFT -> {
                if (dir.equals(Direction.NORTH)) {
                    return rotateShape(Direction.NORTH, Direction.SOUTH, modern_dinner_table_one);}
                else if (dir.equals(Direction.SOUTH)) {
                    return modern_dinner_table_one;}
                else if (dir.equals(Direction.EAST)) {
                    return rotateShape(Direction.NORTH, Direction.WEST, modern_dinner_table_one);}
                else {
                    return rotateShape(Direction.NORTH, Direction.EAST, modern_dinner_table_one);}
            }
            case RIGHT -> {
                if (dir.equals(Direction.NORTH)) {
                    return modern_dinner_table_one;}
                else if (dir.equals(Direction.SOUTH)) {
                    return rotateShape(Direction.NORTH, Direction.SOUTH, modern_dinner_table_one);}
                else if (dir.equals(Direction.EAST)) {
                    return rotateShape(Direction.NORTH, Direction.EAST, modern_dinner_table_one);}
                else {
                    return rotateShape(Direction.NORTH, Direction.WEST, modern_dinner_table_one);}
            }
            case MIDDLE -> {
                if (dirNorthOrSouth) {
                    return modern_dinner_table_middle;
                } else {
                    return rotateShape(Direction.NORTH, Direction.EAST, modern_dinner_table_middle);
                }
            }
            default -> {
                if (dirWestOrEast) {
                    return rotateShape(Direction.NORTH, Direction.EAST, modern_dinner_table);}
                else {
                    return  modern_dinner_table;
                }
            }
        }

    }
}


