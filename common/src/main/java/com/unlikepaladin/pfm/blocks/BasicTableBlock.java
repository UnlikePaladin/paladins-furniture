package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
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

public class BasicTableBlock extends Block {
    private final Block baseBlock;
    public static final EnumProperty<BasicTableShape> SHAPE = EnumProperty.of("shape", BasicTableShape.class);

    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_BASIC_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_BASIC_TABLES = new ArrayList<>();
    public BasicTableBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(SHAPE, BasicTableShape.SINGLE).with(AXIS, Direction.Axis.X));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(BasicTableBlock.class)){
            WOOD_BASIC_TABLES.add(new FurnitureBlock(this, "table_basic"));
        }
        else if (this.getClass().isAssignableFrom(BasicTableBlock.class)){
            STONE_BASIC_TABLES.add(new FurnitureBlock(this, "table_basic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodBasicTables() {
        return WOOD_BASIC_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneBasicTables() {
        return STONE_BASIC_TABLES.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(AXIS);
        stateManager.add(SHAPE);
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> {
                switch (state.get(AXIS)) {
                    case X -> {
                        return state.with(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.with(AXIS, Direction.Axis.X);
                    }
                }
                return state;
            }
        }
        return state;
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
        return direction.getAxis().isHorizontal() ? getShape(state, world, pos) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction.Axis facing = ctx.getPlayerFacing().getAxis();
        BlockState blockState = this.getDefaultState().with(AXIS, facing);
        return getShape(blockState, world, blockPos);
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof BasicTableBlock : blockState.getBlock() == this;
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    /**Done this way to keep the number of states low*/
    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction.Axis dir = state.get(AXIS);
        boolean north = false;
        boolean east = false;
        boolean west = false;
        boolean south = false;
        if (canConnect(world.getBlockState(pos.north())) && world.getBlockState(pos.north()).get(AXIS) == dir) {
            north = canConnect(world.getBlockState(pos.north()));
        }
        if (canConnect(world.getBlockState(pos.east())) && world.getBlockState(pos.east()).get(AXIS) == dir) {
            east =  canConnect(world.getBlockState(pos.east()));
        }

        if (canConnect(world.getBlockState(pos.west())) && world.getBlockState(pos.west()).get(AXIS) == dir) {
            west =  canConnect(world.getBlockState(pos.west()));
        }
        if (canConnect(world.getBlockState(pos.south())) && world.getBlockState(pos.south()).get(AXIS) == dir) {
            south =  canConnect(world.getBlockState(pos.south()));
        }
        boolean cornerNorthWest = north && west && !canConnect(world.getBlockState(pos.north().west()));
        boolean cornerNorthEast = north && east && !canConnect(world.getBlockState(pos.north().east()));
        boolean cornerSouthEast = south && east && !canConnect(world.getBlockState(pos.south().east()));
        boolean cornerSouthWest = south && west && !canConnect(world.getBlockState(pos.south().west()));

        if (north && south && east && west && ((cornerNorthEast && cornerSouthEast) || (cornerSouthWest && cornerNorthWest))){
            if (north && south && east && west && cornerSouthWest && cornerNorthWest) {
                return state.with(SHAPE, BasicTableShape.EAST_EDGE);
            }
            else if (north && south && east && west && cornerNorthEast && cornerSouthEast) {
                return state.with(SHAPE, BasicTableShape.WEST_EDGE);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_SOUTH);
        }
        else if (north && south && east && west && !cornerNorthEast && !cornerSouthEast && !cornerNorthWest && !cornerSouthWest){
            return state.with(SHAPE, BasicTableShape.ALL);
        }
        else if (north && south && east && west && !cornerNorthEast && !cornerSouthEast && cornerNorthWest && !cornerSouthWest){
            if (dir.equals(Direction.Axis.X)) {
                return state.with(SHAPE, BasicTableShape.NORTH_EAST);
            }
            return state.with(SHAPE, BasicTableShape.SOUTH_WEST);
        }
        else if (north && south && east && west && cornerNorthEast && !cornerSouthEast && !cornerNorthWest && !cornerSouthWest){
            if (dir.equals(Direction.Axis.X)) {
                return state.with(SHAPE, BasicTableShape.NORTH_WEST);
            }
            return state.with(SHAPE, BasicTableShape.SOUTH_EAST);
        }
        else if (north && south && east && west && !cornerNorthEast && !cornerSouthEast && !cornerNorthWest && cornerSouthWest){
            if (dir.equals(Direction.Axis.X)) {
                return state.with(SHAPE, BasicTableShape.SOUTH_EAST);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_WEST);
        }
        else if (north && south && east && west && !cornerNorthEast && cornerSouthEast && !cornerNorthWest && !cornerSouthWest){
            if (dir.equals(Direction.Axis.X)) {
                return state.with(SHAPE, BasicTableShape.SOUTH_WEST);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_EAST);
        }
        else if (cornerNorthEast && cornerSouthEast) {
            if (north && south && east && west) {
                return state.with(SHAPE, BasicTableShape.EAST_EDGE);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_SOUTH);
        }
        else if (cornerNorthWest && cornerSouthWest) {
            if (north && south && east && west) {
                return state.with(SHAPE, BasicTableShape.EAST_EDGE);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_SOUTH);
        }
        else if (cornerNorthEast && cornerNorthWest) {
            return state.with(SHAPE, BasicTableShape.EAST_WEST);
        }
        else if (cornerSouthWest && cornerSouthEast) {
            return state.with(SHAPE, BasicTableShape.EAST_WEST);
        }
        else if (cornerNorthEast) {
            return state.with(SHAPE, BasicTableShape.CORNER_NORTH_EAST);
        }
        else if (cornerNorthWest) {
            return state.with(SHAPE, BasicTableShape.CORNER_NORTH_WEST);
        }
        else if (cornerSouthEast) {
            return state.with(SHAPE, BasicTableShape.CORNER_SOUTH_EAST);
        }
        else if (cornerSouthWest) {
            return state.with(SHAPE, BasicTableShape.CORNER_SOUTH_WEST);
        }
        else if (north && south && !east && !west) {
            if (north && south && east && west) {
                return state.with(SHAPE, BasicTableShape.EAST_EDGE);
            }
            return state.with(SHAPE, BasicTableShape.NORTH_SOUTH);
        }
        else if (!north && !south && east && west) {
            return state.with(SHAPE, BasicTableShape.EAST_WEST);
        }
        else if (!north && south && east && !west) {
            return state.with(SHAPE, BasicTableShape.SOUTH_EAST);
        }
        else if (!north && south && !east && west) {
            return state.with(SHAPE, BasicTableShape.SOUTH_WEST);
        }
        else if (north && !south && !east && west) {
            return state.with(SHAPE, BasicTableShape.NORTH_WEST);
        }
        else if (north && !south && east && !west) {
            return state.with(SHAPE, BasicTableShape.NORTH_EAST);
        }
        else if (north && !south && east && west) {
            return state.with(SHAPE, BasicTableShape.NORTH_EDGE);
        }
        else if (!north && south && east && west) {
            return state.with(SHAPE, BasicTableShape.SOUTH_EDGE);
        }
        else if (north && south && east && !west) {
            return state.with(SHAPE, BasicTableShape.EAST_EDGE);
        }
        else if (north && south && !east && west) {
            return state.with(SHAPE, BasicTableShape.WEST_EDGE);
        }
        else if (north && !south && !east && !west) {
            return state.with(SHAPE, BasicTableShape.NORTH);
        }
        else if (!north && south && !east && !west) {
            return state.with(SHAPE, BasicTableShape.SOUTH);
        }
        else if (!north && !south && east && !west) {
            return state.with(SHAPE, BasicTableShape.EAST);
        }
        else if (!north && !south && !east && west) {
            return state.with(SHAPE, BasicTableShape.WEST);
        }
        return state.with(SHAPE, BasicTableShape.SINGLE);
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
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 0, 14, 2, 12), createCuboidShape(2, 0, 0, 4, 14, 2));
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 12, 4, 14, 14), createCuboidShape(2, 0, 0, 4, 2, 12), createCuboidShape(12, 0, 0, 14, 14, 2));

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
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_NORTH_WEST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_EAST_FACING_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_BASIC_CORNER_NORTH_EAST);
    final static VoxelShape TABLE_BASIC_CORNER_NORTH_WEST_FACING_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_BASIC_CORNER_NORTH_WEST);

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction.Axis dir = state.get(AXIS);
        boolean dirNorthOrSouth = dir.equals(Direction.Axis.X);
        BasicTableShape blockShape = state.get(SHAPE);
        if (dirNorthOrSouth) {
            return switch (blockShape) {
                case NORTH -> TABLE_BASIC_NORTH;
                case WEST -> TABLE_BASIC_EAST_FACING_WEST;
                case EAST -> TABLE_BASIC_EAST_FACING_EAST;
                case SOUTH -> TABLE_BASIC_NORTH_FACING_SOUTH;
                case CORNER_NORTH_EAST -> TABLE_BASIC_CORNER_NORTH_EAST;
                case NORTH_SOUTH -> TABLE_BASIC_NORTH_SOUTH;
                case CORNER_NORTH_WEST -> TABLE_BASIC_CORNER_NORTH_WEST;
                case CORNER_SOUTH_EAST -> TABLE_BASIC_CORNER_NORTH_WEST_FACING_SOUTH;
                case CORNER_SOUTH_WEST -> TABLE_BASIC_CORNER_NORTH_EAST_FACING_SOUTH;
                case EAST_EDGE -> TABLE_BASIC_LEG_FACING_EAST;
                case WEST_EDGE -> TABLE_BASIC_LEG_FACING_WEST;
                case NORTH_EDGE,SOUTH_EDGE,ALL,EAST_WEST -> TABLE_BASIC_MIDDLE;
                case NORTH_EAST -> TABLE_BASIC_NORTH_EAST;
                case NORTH_WEST-> TABLE_BASIC_NORTH_WEST_NORTH_FACING_EAST;
                case SOUTH_EAST -> TABLE_BASIC_NORTH_WEST_FACING_WEST;
                case SOUTH_WEST -> TABLE_BASIC_NORTH_EAST_FACING_SOUTH;
                default -> TABLE_BASIC;
                };
            }
        else {
            return switch (blockShape) {
                case NORTH -> TABLE_BASIC_EAST;
                case WEST -> TABLE_BASIC_NORTH_FACING_WEST;
                case EAST -> TABLE_BASIC_NORTH_FACING_EAST;
                case SOUTH -> TABLE_BASIC_EAST_FACING_SOUTH;
                case CORNER_NORTH_EAST -> TABLE_BASIC_CORNER_NORTH_WEST_FACING_EAST;
                case EAST_WEST -> TABLE_BASIC_NORTH_SOUTH_FACING_EAST;
                case CORNER_NORTH_WEST -> TABLE_BASIC_CORNER_NORTH_EAST_FACING_WEST ;
                case CORNER_SOUTH_EAST -> TABLE_BASIC_CORNER_NORTH_EAST_FACING_EAST;
                case CORNER_SOUTH_WEST -> TABLE_BASIC_CORNER_NORTH_WEST_FACING_WEST;
                case NORTH_EDGE -> TABLE_BASIC_LEG;
                case SOUTH_EDGE -> TABLE_BASIC_LEG_FACING_SOUTH;
                case EAST_EDGE,WEST_EDGE,ALL,NORTH_SOUTH -> TABLE_BASIC_MIDDLE;
                case NORTH_EAST -> TABLE_BASIC_NORTH_WEST_FACING_EAST;
                case NORTH_WEST-> TABLE_BASIC_NORTH_EAST_FACING_WEST;
                case SOUTH_EAST -> TABLE_BASIC_NORTH_EAST_FACING_EAST;
                case SOUTH_WEST -> TABLE_BASIC_NORTH_WEST;
                default -> TABLE_BASIC_FACING_EAST;
            };
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}

enum BasicTableShape implements StringIdentifiable {
    CORNER_NORTH_EAST("corner_north_east"),
    CORNER_SOUTH_EAST("corner_south_east"),
    CORNER_NORTH_WEST("corner_north_west"),
    CORNER_SOUTH_WEST("corner_south_west"),
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west"),
    ALL("all"),
    SINGLE("single"),
    NORTH_SOUTH("north_south"),
    EAST_WEST("east_west"),
    SOUTH_WEST("south_west"),
    NORTH_WEST("north_west"),
    SOUTH_EAST("south_east"),
    NORTH_EAST("north_east"),
    NORTH_EDGE("north_edge"),
    SOUTH_EDGE("south_edge"),
    EAST_EDGE("east_edge"),
    WEST_EDGE("west_edge");

    private final String name;

    BasicTableShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}


