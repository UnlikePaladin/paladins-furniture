package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;
import java.util.stream.Stream;

public class BasicTableBlock extends Block {
    private final Block baseBlock;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_BASIC_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_BASIC_TABLES = new ArrayList<>();
    public BasicTableBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.X));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(BasicTableBlock.class)){
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(AXIS);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }
    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction.Axis facing = ctx.getHorizontalDirection().getAxis();
        return this.defaultBlockState().setValue(AXIS, facing);
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    public boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof BasicTableBlock : blockState.getBlock() == this;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    final static VoxelShape TABLE_BASIC_BASE = box(0, 14, 0, 16, 16, 16);
    final static VoxelShape TABLE_BASIC_NORTH_EAST_LEG = box(12, 0, 2, 14, 14, 4);
    final static VoxelShape TABLE_BASIC_SOUTH_WEST_LEG = box(2, 0, 12,4, 14, 14);
    final static VoxelShape TABLE_BASIC_NORTH_WEST_LEG = box(2, 0, 2,4, 14, 4);
    final static VoxelShape TABLE_BASIC_SOUTH_EAST_LEG = box(12, 0, 12,14, 14, 14);
    final static VoxelShape TABLE_BASIC_EAST_WEST_NORTH = box(4, 0, 2,12, 2, 4);
    final static VoxelShape TABLE_BASIC_EAST_WEST_SOUTH = box(4, 0, 12,12, 2, 14);
    final static VoxelShape TABLE_BASIC_SOUTH_EAST_TOP = box(4, 0, 2,16, 2, 4);
    final static VoxelShape TABLE_BASIC_SOUTH_EAST_BOTTOM = box(4, 0, 12,16, 2, 14);
    final static VoxelShape TABLE_BASIC_SOUTH_WEST_BOTTOM = box(0, 0, 12,12, 2, 14);
    final static VoxelShape TABLE_BASIC_SOUTH_WEST_TOP = box(0, 0, 2,12, 2, 4);
    final static VoxelShape TABLE_BASIC_NORTH_SOUTH_WEST = box(0, 0, 2,16, 2, 4);
    final static VoxelShape TABLE_BASIC_NORTH_SOUTH_EAST = box(0, 0, 12, 16, 2, 14);
    final static VoxelShape TABLE_BASIC_NORTH_EAST_CORNER = box(14, 0, 2, 16, 2, 4);
    final static VoxelShape TABLE_BASIC_SOUTH_EAST_CORNER = box(14, 0, 12, 16, 2, 14);
    final static VoxelShape TABLE_BASIC_NORTH_WEST_CORNER = box(0, 0, 2, 2, 2, 4);
    final static VoxelShape TABLE_BASIC_SOUTH_WEST_CORNER = box(0, 0, 12, 2, 2, 14);
    final static Map<String, VoxelShape> VOXEL_SHAPES = new HashMap<>();

    public boolean canConnect(BlockGetter world, BlockState state, BlockPos neighborPos, BlockPos pos){
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.hasProperty(AXIS)) {
            return neighborState.getValue(AXIS) == state.getValue(AXIS) && canConnect(neighborState);
        }
        return false;
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction.Axis dir = state.getValue(BasicTableBlock.AXIS);

        Boolean north = canConnect(world, state, pos.north(), pos);
        boolean east = canConnect(world, state, pos.east(), pos);
        boolean west = canConnect(world, state, pos.west(), pos);
        boolean south = canConnect(world, state, pos.south(), pos);
        boolean cornerNorthWest = north && west && !canConnect(world, state, pos.north().west(), pos);
        boolean cornerNorthEast = north && east && !canConnect(world, state, pos.north().east(), pos);
        boolean cornerSouthEast = south && east && !canConnect(world, state, pos.south().east(), pos);
        boolean cornerSouthWest = south && west && !canConnect(world, state, pos.south().west(), pos);

        String key = north.toString()+ east + west + south + cornerNorthWest + cornerNorthEast + cornerSouthEast + cornerSouthWest + dir.getSerializedName();
        if (!VOXEL_SHAPES.containsKey(key)) {
            generateVoxelShape(key, north, east, west, south, cornerNorthWest, cornerNorthEast, cornerSouthEast, cornerSouthWest, dir);
        }
        return VOXEL_SHAPES.get(key);
    }

    private static void generateVoxelShape(String key, Boolean north, Boolean east, Boolean west, Boolean south, Boolean cornerNorthWest, Boolean cornerNorthEast, Boolean cornerSouthEast, Boolean cornerSouthWest, Direction.Axis axis) {
        VoxelShape newVoxelShape = TABLE_BASIC_BASE;
        if (!north && !south && !east && !west) {
            newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_EAST_WEST_NORTH, TABLE_BASIC_EAST_WEST_SOUTH);
        }
        if (!north && !east) {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, TABLE_BASIC_NORTH_EAST_LEG, TABLE_BASIC_NORTH_WEST_LEG));
        }
        if (!north && !west)  {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, TABLE_BASIC_NORTH_WEST_LEG, TABLE_BASIC_SOUTH_WEST_LEG));
        }
        if (!south && !east)  {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, TABLE_BASIC_SOUTH_EAST_LEG, TABLE_BASIC_NORTH_EAST_LEG));
        }
        if (!south && !west)  {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, TABLE_BASIC_SOUTH_WEST_LEG, TABLE_BASIC_SOUTH_EAST_LEG));
        }
        if (cornerNorthEast) {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, Shapes.or(TABLE_BASIC_NORTH_EAST_LEG, TABLE_BASIC_NORTH_EAST_CORNER), Shapes.or(TABLE_BASIC_NORTH_WEST_LEG, TABLE_BASIC_NORTH_WEST_CORNER)));
        }
        if (cornerNorthWest) {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, Shapes.or(TABLE_BASIC_NORTH_WEST_LEG, TABLE_BASIC_NORTH_WEST_CORNER), Shapes.or(TABLE_BASIC_SOUTH_WEST_LEG, TABLE_BASIC_SOUTH_WEST_CORNER)));
        }
        if (cornerSouthWest) {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, Shapes.or(TABLE_BASIC_SOUTH_WEST_LEG, TABLE_BASIC_SOUTH_WEST_CORNER), Shapes.or(TABLE_BASIC_SOUTH_EAST_LEG, TABLE_BASIC_SOUTH_EAST_CORNER)));
        }
        if (cornerSouthEast) {
            newVoxelShape = Shapes.or(newVoxelShape, getShapeForAxis(axis, Shapes.or(TABLE_BASIC_SOUTH_EAST_LEG, TABLE_BASIC_SOUTH_EAST_CORNER), Shapes.or(TABLE_BASIC_NORTH_EAST_LEG, TABLE_BASIC_NORTH_EAST_CORNER)));
        }

        if (axis == Direction.Axis.Z) {
            if (!north && south && !east && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_EAST_WEST_NORTH);
            }
            if (north && !south && !east && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_EAST_WEST_SOUTH);
            }
            if (!north && east && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_EAST_TOP);
            }
            if (!south && !east && west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_WEST_BOTTOM);
            }
            if (!south && east && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_EAST_BOTTOM);
            }
            if (!north && !east && west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_WEST_TOP);
            }
            if (!north && east && west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_NORTH_SOUTH_WEST);
            }
            if (!south && east && west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_NORTH_SOUTH_EAST);
            }
        }
        else {
            if (!north && south && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_EAST_BOTTOM);
            }
            if (north && !south && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_WEST_BOTTOM);
            }
            if (!north && south && !east) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_EAST_TOP);
            }
            if (north && !south && !east) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_WEST_TOP);
            }
            if (north && !south && !east) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_SOUTH_WEST_TOP);
            }
            if (!north && !south && !east) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_EAST_WEST_NORTH);
            }
            if (!north && !south && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_EAST_WEST_SOUTH);
            }
            if (north && south && !east) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_NORTH_SOUTH_WEST);
            }
            if (north && south && !west) {
                newVoxelShape = Shapes.or(newVoxelShape, TABLE_BASIC_NORTH_SOUTH_EAST);
            }
            newVoxelShape = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, newVoxelShape);
        }
        VOXEL_SHAPES.put(key, newVoxelShape);
    }

    private static VoxelShape getShapeForAxis(Direction.Axis axis, VoxelShape a, VoxelShape b) {
        if (axis == Direction.Axis.Z) {
            return a;
        } else if (axis == Direction.Axis.X) {
            return b;
        }
        return Shapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> {
                switch (state.getValue(AXIS)) {
                    case X -> {
                        return state.setValue(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.setValue(AXIS, Direction.Axis.X);
                    }
                }
                return state;
            }
        }
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(Direction.get(Direction.AxisDirection.NEGATIVE, state.getValue(AXIS))));
    }
}


