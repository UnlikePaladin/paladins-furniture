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
import net.minecraft.util.BlockRotation;
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

public class BasicTable extends HorizontalFacingBlock implements Waterloggable{
    private final Block baseBlock;
    public static final EnumProperty<MiddleShape> TYPE = EnumProperty.of("type", MiddleShape.class);
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty CORNER_NORTH_WEST = BooleanProperty.of("corner_north_west");
    public static final BooleanProperty CORNER_NORTH_EAST = BooleanProperty.of("corner_north_east");
    public static final BooleanProperty CORNER_SOUTH_EAST = BooleanProperty.of("corner_south_east");
    public static final BooleanProperty CORNER_SOUTH_WEST = BooleanProperty.of("corner_south_west");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_BASIC_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_BASIC_TABLES = new ArrayList<>();
    public BasicTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(NORTH, false).with(SOUTH,false).with(EAST,false).with(WEST,false).with(WATERLOGGED, false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(BasicTable.class)){
            WOOD_BASIC_TABLES.add(new FurnitureBlock(this, "table_basic"));
        }
        else if (this.getClass().isAssignableFrom(BasicTable.class)){
            STONE_BASIC_TABLES.add(new FurnitureBlock(this, "table_basic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodBasicTables() {
        return WOOD_BASIC_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneBasicTables() {
        return STONE_BASIC_TABLES.stream();
    }

    //TODO: Optimize the shit out of this
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(NORTH);
        stateManager.add(EAST);
        stateManager.add(WEST);
        stateManager.add(SOUTH);
        stateManager.add(CORNER_NORTH_EAST);
        stateManager.add(CORNER_NORTH_WEST);
        stateManager.add(CORNER_SOUTH_EAST);
        stateManager.add(CORNER_SOUTH_WEST);
        stateManager.add(WATERLOGGED);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }
    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return direction.getAxis().isHorizontal() ? getShape(state, world, pos) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        return getShape(blockState, world, blockPos);
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }


    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction dir = state.get(FACING);
        boolean north = false;
        boolean east = false;
        boolean west = false;
        boolean south = false;
        if (world.getBlockState(pos.north()).getBlock() == this && world.getBlockState(pos.north()).get(FACING) == dir) {
            north = world.getBlockState(pos.north()).getBlock() == this;
        }
        if (world.getBlockState(pos.east()).getBlock() == this && world.getBlockState(pos.east()).get(FACING) == dir) {
            east = world.getBlockState(pos.east()).getBlock() == this;
        }

        if (world.getBlockState(pos.west()).getBlock() == this && world.getBlockState(pos.west()).get(FACING) == dir) {
            west = world.getBlockState(pos.west()).getBlock() == this;
        }
        if (world.getBlockState(pos.south()).getBlock() == this && world.getBlockState(pos.south()).get(FACING) == dir) {
            south = world.getBlockState(pos.south()).getBlock() == this;
        }
        boolean cornerNorthWest = north && west && world.getBlockState(pos.north().west()).getBlock() != this;
        boolean cornerNorthEast = north && east && world.getBlockState(pos.north().east()).getBlock() != this;
        boolean cornerSouthEast = south && east && world.getBlockState(pos.south().east()).getBlock() != this;
        boolean cornerSouthWest = south && west && world.getBlockState(pos.south().west()).getBlock() != this;
        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(CORNER_NORTH_WEST, cornerNorthWest).with(CORNER_NORTH_EAST, cornerNorthEast).with(CORNER_SOUTH_EAST, cornerSouthEast).with(CORNER_SOUTH_WEST, cornerSouthWest);
    }

    /** Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/ */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    final static VoxelShape TABLE_BASIC_NORTH = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(2, 0, 0, 4, 2, 12), createCuboidShape(12, 0, 0, 14, 2, 12), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape TABLE_BASIC_EAST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 11,4, 14, 13), createCuboidShape(12, 0, 11, 14, 14, 13), createCuboidShape(4, 0,11, 12, 2, 13));
    final static VoxelShape TABLE_BASIC = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(2, 0, 2, 4, 14, 4), createCuboidShape(2, 0, 4, 4, 2, 12), createCuboidShape(12, 0, 4, 14, 2, 12), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape TABLE_BASIC_NORTH_WEST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 3, 14, 14, 5), createCuboidShape(0, 0, 3, 12, 2, 5));
    final static VoxelShape TABLE_BASIC_NORTH_EAST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(3, 0, 12, 5, 14, 14), createCuboidShape(3, 0, 0, 5, 2, 12));
    final static VoxelShape TABLE_BASIC_MIDDLE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16));
    final static VoxelShape TABLE_BASIC_NORTH_SOUTH = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 0, 4, 2, 16), createCuboidShape(12, 0, 0, 14, 2, 16));
    final static VoxelShape TABLE_BASIC_LEG = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(0, 0, 11, 16, 2, 13));
    final static VoxelShape TABLE_BASIC_DOUBLE_CORNER = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(12, 0, 0, 14, 14, 2), createCuboidShape(2, 0, 0, 4, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 0, 14, 2, 12), createCuboidShape(2, 0, 0, 4, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 12, 4, 14, 14), createCuboidShape(2, 0, 0, 4, 2, 12), createCuboidShape(12, 0, 0, 14, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 0, 4, 14, 2), createCuboidShape(12, 0, 0, 14, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER_M = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 3, 4, 14, 5), createCuboidShape(12, 0, 3, 14, 14, 5), createCuboidShape(14, 0, 3, 16, 2, 5), createCuboidShape(0, 0, 3, 2, 2, 5));
    final static VoxelShape TABLE_BASIC_CORNER_TRI = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(11, 0, 0, 13, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER_TRI_WEST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(3, 0, 0, 5, 14, 2));

    final static VoxelShape TABLE_BASIC_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC);
    final static VoxelShape TABLE_BASIC_EAST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_EAST);
    final static VoxelShape TABLE_BASIC_NORTH_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_NORTH);
    final static VoxelShape TABLE_BASIC_NORTH_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_NORTH);
    final static VoxelShape TABLE_BASIC_EAST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_EAST);
    final static VoxelShape TABLE_BASIC_EAST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_EAST);
    final static VoxelShape TABLE_BASIC_NORTH_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_NORTH);
    final static VoxelShape TABLE_BASIC_NORTH_WEST_FACING_EAST = rotateShape(Direction.WEST, Direction.EAST, TABLE_BASIC_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_NORTH_WEST_NORTH_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_NORTH_EAST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_NORTH_WEST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_NORTH_EAST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_NORTH_EAST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_NORTH_SOUTH_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_NORTH_SOUTH);
    final static VoxelShape TABLE_BASIC_LEG_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_LEG);
    final static VoxelShape TABLE_BASIC_LEG_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_LEG);
    final static VoxelShape TABLE_BASIC_LEG_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_LEG);
    final static VoxelShape TABLE_BASIC_DOUBLE_CORNER_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_DOUBLE_CORNER);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_CORNER_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER);
    final static VoxelShape TABLE_BASIC_CORNER_M_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_M);
    final static VoxelShape TABLE_BASIC_CORNER_M_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER_M);
    final static VoxelShape TABLE_BASIC_CORNER_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER);
    final static VoxelShape TABLE_BASIC_CORNER_M_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER_M);
    final static VoxelShape TABLE_BASIC_CORNER_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER);
    final static VoxelShape TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_TRI_WEST);

    //Cursed I know, probably will rewrite this at some point in the future
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        boolean dirNorthOrSouth = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
        boolean dirWestOrEast = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);
        boolean north = state.get(NORTH);
        boolean east = state.get(EAST);
        boolean west = state.get(WEST);
        boolean south = state.get(SOUTH);
        boolean cornerNorthWest = state.get(CORNER_NORTH_WEST);
        boolean cornerNorthEast = state.get(CORNER_NORTH_EAST);
        boolean cornerSouthEast = state.get(CORNER_SOUTH_EAST);
        boolean cornerSouthWest = state.get(CORNER_SOUTH_WEST);

        if (dirNorthOrSouth && !(north || south || west || east)) {
            return TABLE_BASIC;
        } else if (dirWestOrEast && !(north || south || west || east)) {
            return TABLE_BASIC_FACING_EAST;
        }
        if (dirNorthOrSouth && north && !(south || west || east)) {
            return TABLE_BASIC_NORTH;
        } else if (dirWestOrEast && north && !(south || west || east)) {
            return TABLE_BASIC_EAST;
        }
        if (dirNorthOrSouth && east && !(south || west || north)) {
            return TABLE_BASIC_EAST_FACING_EAST;
        } else if (dirWestOrEast && east && !(south || west || north)) {
            return TABLE_BASIC_NORTH_FACING_EAST;
        }
        if (dirNorthOrSouth && south && !(west || east || north)) {
            return TABLE_BASIC_NORTH_FACING_SOUTH;
        } else if (dirWestOrEast && south && !(west || east || north)) {
            return TABLE_BASIC_EAST_FACING_SOUTH;
        }
        if (dirNorthOrSouth && west && !(south || east || north)) {
            return TABLE_BASIC_EAST_FACING_WEST;
        } else if (dirWestOrEast && west && !(south || east || north)) {
            return TABLE_BASIC_NORTH_FACING_WEST;
        }
        if (dirNorthOrSouth && (east && north) && !(south || west || cornerNorthEast)) {
            return TABLE_BASIC_NORTH_EAST;
        } else if (dirWestOrEast && (east && north) && !(south || west || cornerNorthEast)) {
            return TABLE_BASIC_NORTH_WEST_FACING_EAST;
        }
        if (dirNorthOrSouth && (west && north) && !(south || east || cornerNorthWest)) {
            return TABLE_BASIC_NORTH_WEST_NORTH_FACING_EAST;
        } else if (dirWestOrEast && (west && north) && !(south || east || cornerNorthWest)) {
            return TABLE_BASIC_NORTH_EAST_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && east) && !(west || north || cornerSouthEast)) {
            return TABLE_BASIC_NORTH_WEST_FACING_WEST;
        } else if (dirWestOrEast && (south && east) && !(west || north || cornerSouthEast)) {
            return TABLE_BASIC_NORTH_EAST_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && west) && !(east || north || cornerSouthWest)) {
            return TABLE_BASIC_NORTH_EAST_FACING_SOUTH;
        } else if (dirWestOrEast && (south && west) && !(east || north || cornerSouthWest)) {
            return TABLE_BASIC_NORTH_WEST;
        }
        if (dirNorthOrSouth && (south && north) && !(east || west)) {
            return TABLE_BASIC_NORTH_SOUTH;
        } else if (dirWestOrEast && (south && north) && !(east || west)) {
            return TABLE_BASIC_MIDDLE;
        }
        if (dirNorthOrSouth && (east && west) && !(north || south)) {
            return TABLE_BASIC_MIDDLE;
        } else if (dirWestOrEast && (east && west) && !(north || south)) {
            return TABLE_BASIC_NORTH_SOUTH_FACING_EAST;
        }
        if (dirNorthOrSouth && (north && west && south) && !(east || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_LEG_FACING_WEST;
        } else if (dirWestOrEast && (north && west && south) && !(east || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_MIDDLE;
        }
        if (dirNorthOrSouth && (north && east && south) && !(west || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_LEG_FACING_EAST;
        } else if (dirWestOrEast && (north && east && south) && !(west || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_MIDDLE;
        }
        if (dirNorthOrSouth && (north && east && west) && !(south || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_MIDDLE;
        } else if (dirWestOrEast && (north && east && west) && !(south || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_LEG;
        }
        if (dirNorthOrSouth && (south && east && west) && !( north || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_MIDDLE;
        } else if (dirWestOrEast && (south && east && west) && !(north || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_LEG_FACING_SOUTH;
        }
        if (dirNorthOrSouth && (north && east && west && cornerNorthWest && cornerNorthEast) && !( south || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER;
        } else if (dirWestOrEast && (north && east && west && cornerNorthWest && cornerNorthEast) && !( south || cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && east && west && cornerSouthEast && cornerSouthWest) && !( north || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER;
        } else if (dirWestOrEast && (south && east && west && cornerSouthEast && cornerSouthWest) && !( north || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && east && north && cornerNorthEast && cornerSouthEast) && !( west || cornerNorthWest || cornerSouthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER;
        } else if (dirWestOrEast && (south && east && north && cornerNorthEast && cornerSouthEast) && !( west  || cornerNorthWest || cornerSouthWest)) {
            return TABLE_BASIC_DOUBLE_CORNER_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && cornerSouthWest) && !( east || cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_DOUBLE_CORNER;
        } else if (dirWestOrEast && (south && west && north && cornerNorthWest && cornerSouthWest) && !( east || cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_DOUBLE_CORNER_FACING_EAST;
        }
        if ((south && west && north && east) && !(cornerSouthWest || cornerNorthWest || cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_MIDDLE;
        }
        if (dirNorthOrSouth && cornerNorthWest && !(cornerSouthWest || cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_CORNER_NORTH_WEST;
        } else if (dirWestOrEast && cornerNorthWest && !(cornerSouthWest || cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_CORNER_NORTH_EAST_FACING_WEST;
        }
        if (dirNorthOrSouth && cornerNorthEast  && !(cornerSouthWest || cornerSouthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_EAST;
        } else if (dirWestOrEast && cornerNorthEast && !(cornerSouthWest || cornerSouthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_WEST_FACING_EAST;
        }
        if (dirNorthOrSouth && cornerSouthEast  && !(cornerSouthWest || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_WEST_FACING_SOUTH;
        } else if (dirWestOrEast && cornerSouthEast && !(cornerSouthWest || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_EAST_FACING_EAST;
        }
        if (dirNorthOrSouth && cornerSouthWest  && !(cornerSouthEast || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_EAST_FACING_SOUTH;
        } else if (dirWestOrEast && cornerSouthWest && !(cornerSouthEast || cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_NORTH_WEST_FACING_WEST;
        }
        if (dirNorthOrSouth && south && west && north && cornerNorthWest && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) {
            return TABLE_BASIC_DOUBLE_CORNER;}
           else if (dirWestOrEast && south && west && north && cornerNorthWest && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) {
            return TABLE_BASIC_DOUBLE_CORNER_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerNorthEast) && !(cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_CORNER;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && cornerNorthEast && east) && !(cornerSouthEast || cornerSouthWest)) {
            return TABLE_BASIC_CORNER_M;
        }
        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerSouthEast) && !(cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && cornerSouthEast && east) && !(cornerNorthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_M_FACING_SOUTH;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthEast && east && cornerSouthEast) && !(cornerSouthWest || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_M_FACING_EAST;}
        else if (dirWestOrEast && (south && west && north && cornerNorthEast && cornerSouthEast && east) && !(cornerSouthWest || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_FACING_EAST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerNorthWest) && !(cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_CORNER_M_FACING_WEST;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && cornerNorthWest && east) && !(cornerSouthEast || cornerNorthEast)) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) && !cornerNorthWest) {
            return TABLE_BASIC_CORNER_TRI;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && east && cornerNorthEast && cornerSouthEast) && !cornerNorthWest) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthEast && cornerNorthEast) && !cornerSouthWest) {
            return TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerNorthEast && cornerSouthEast) && !cornerSouthWest) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthEast) && !(cornerNorthEast || cornerSouthWest)) {
            return TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerSouthEast) && !(cornerSouthWest || cornerNorthEast)) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthWest && cornerSouthEast) && !cornerNorthEast) {
            return TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerSouthEast && cornerSouthWest) && !cornerNorthEast) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthWest && cornerNorthEast) && !cornerSouthEast) {
            return TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerNorthEast && cornerSouthWest) && !cornerSouthEast) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        if (dirNorthOrSouth && (south && west && north && east && cornerSouthWest && cornerNorthEast) && !(cornerSouthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_TRI_WEST_FACING_SOUTH;}
        else if (dirWestOrEast && (south && west && north && east && cornerSouthWest && cornerNorthEast) && !(cornerSouthEast || cornerNorthWest)) {
            return TABLE_BASIC_CORNER_FACING_WEST;
        }
        else
        {
            return VoxelShapes.fullCube();
        }
    }
}


