package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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

public class ModernDinnerTable extends Block implements Waterloggable{
    private final Block baseBlock;
    public static final EnumProperty<MiddleShape> SHAPE = EnumProperty.of("table_type", MiddleShape.class);
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final List<FurnitureBlock> WOOD_DINNER_MODERN_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DINNER_MODERN_TABLES = new ArrayList<>();

    private final BlockState baseBlockState;

    public ModernDinnerTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(SHAPE, MiddleShape.SINGLE).with(AXIS, Direction.Axis.X).with(WATERLOGGED, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ModernDinnerTable.class)){
            WOOD_DINNER_MODERN_TABLES.add(new FurnitureBlock(this, "table_modern_dinner"));
        }
        else if (this.getClass().isAssignableFrom(ModernDinnerTable.class)){
            STONE_DINNER_MODERN_TABLES.add(new FurnitureBlock(this, "table_modern_dinner"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodModernDinnerTables() {
        return WOOD_DINNER_MODERN_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneModernDinnerTables() {
        return STONE_DINNER_MODERN_TABLES.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(SHAPE);
        stateManager.add(AXIS);
        stateManager.add(WATERLOGGED);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(AXIS, ctx.getPlayerFacing().rotateYClockwise().getAxis()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        return getShape(blockState, ctx.getWorld(), ctx.getBlockPos(), blockState.get(AXIS));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return direction.getAxis().isHorizontal() ? getShape(state, world, pos, state.get(AXIS)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private boolean isTable(WorldAccess world, BlockPos pos, Direction.Axis direction, int i)
    {
        BlockState state = world.getBlockState(pos.offset(direction, i));
        if(state.getBlock() == this)
        {
            Direction.Axis sourceDirection = state.get(AXIS);
            return sourceDirection.equals(direction);
        }
        return false;
    }

    public BlockState getShape(BlockState state, WorldAccess world, BlockPos pos, Direction.Axis dir)
    {
        boolean left = isTable(world, pos, dir, -1);
        boolean right = isTable(world, pos, dir, 1);
        if(left && right)
        {
            return state.with(SHAPE, MiddleShape.MIDDLE);
        }
        else if(left)
        {
            return state.with(SHAPE, MiddleShape.RIGHT);
        }
        else if(right)
        {
            return state.with(SHAPE, MiddleShape.LEFT);
        }
        return state.with(SHAPE, MiddleShape.SINGLE);
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


    protected MiddleShape getShape(BlockState state) {
        return state.get(this.SHAPE);
    }

    final static VoxelShape MODERN_DINNER_TABLE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(13, 2, 7,15, 14, 9), createCuboidShape(1, 2, 7, 3, 14, 9),createCuboidShape(2, 0, 2,4, 14, 4),createCuboidShape(2, 0, 4, 4, 2, 12), createCuboidShape(3, 2, 7,13, 4, 9),createCuboidShape(12, 0, 4,14, 2, 12), createCuboidShape(2, 0, 12,4, 14, 14));
    final static VoxelShape MODERN_DINNER_TABLE_MIDDLE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16),createCuboidShape(0, 2, 7,16, 4, 9 ));
    final static VoxelShape MODERN_DINNER_TABLE_ONE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 2, 7, 15, 14, 9), createCuboidShape(12, 0, 12,14, 14, 14), createCuboidShape(12, 0, 4,14, 2, 12 ), createCuboidShape(0, 2, 7,13, 4, 9), createCuboidShape(12, 0, 2,14, 14, 4 ));
    final static VoxelShape MODERN_DINNER_TABLE_ONE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_ONE_WEST = rotateShape(Direction.NORTH, Direction.WEST, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_ONE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE_MIDDLE);
    final static VoxelShape MODERN_DINNER_TABLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE);

    //Cursed I know
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        MiddleShape tableShape = getShape(state);
        Direction.Axis dir = state.get(AXIS);
        boolean dirNorthOrSouth = dir.equals(Direction.Axis.X);
        boolean dirWestOrEast = dir.equals(Direction.Axis.Z);

        switch (tableShape) {
            case LEFT -> {
                if (dirNorthOrSouth) {
                    return MODERN_DINNER_TABLE_ONE_SOUTH;}
                else {
                    return MODERN_DINNER_TABLE_ONE_WEST;}
            }
            case RIGHT -> {
                if (dirNorthOrSouth) {
                    return MODERN_DINNER_TABLE_ONE;}
                else {
                    return MODERN_DINNER_TABLE_ONE_EAST;}
            }
            case MIDDLE -> {
                if (dirNorthOrSouth) {
                    return MODERN_DINNER_TABLE_MIDDLE;
                } else {
                    return MODERN_DINNER_TABLE_MIDDLE_EAST;
                }
            }
            default -> {
                if (dirWestOrEast) {
                    return MODERN_DINNER_TABLE_EAST;}
                else {
                    return MODERN_DINNER_TABLE;
                }
            }
        }

    }
}


