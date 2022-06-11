package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.*;
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

public class BasicTable extends HorizontalFacingBlock {

    private final Block baseBlock;
    public static final EnumProperty<TableShape> TYPE = EnumProperty.of("type", TableShape.class);
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty CORNER_NORTH_WEST = BooleanProperty.of("corner_north_west");
    public static final BooleanProperty CORNER_NORTH_EAST = BooleanProperty.of("corner_north_east");
    public static final BooleanProperty CORNER_SOUTH_EAST = BooleanProperty.of("corner_south_east");
    public static final BooleanProperty CORNER_SOUTH_WEST = BooleanProperty.of("corner_south_west");

    private final BlockState baseBlockState;
    public BasicTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(NORTH, false).with(SOUTH,false).with(EAST,false).with(WEST,false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }


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

        return direction.getAxis().isHorizontal() ? getShape(state, world, pos) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return getShape(blockState, world, blockPos);
    }


    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction dir = state.get(FACING);
        boolean north = false;
        boolean east = false;
        boolean west = false;
        boolean south = false;
      if (world.getBlockState(pos.north()).getBlock() == this && world.getBlockState(pos.north()).get(FACING) == dir){
          north = world.getBlockState(pos.north()).getBlock() == this;}

      if (world.getBlockState(pos.east()).getBlock() == this && world.getBlockState(pos.east()).get(FACING) == dir){
          east = world.getBlockState(pos.east()).getBlock() == this;}

      if (world.getBlockState(pos.west()).getBlock() == this && world.getBlockState(pos.west()).get(FACING) == dir){
            west = world.getBlockState(pos.west()).getBlock() == this;}

      if (world.getBlockState(pos.south()).getBlock() == this && world.getBlockState(pos.south()).get(FACING) == dir){
            south = world.getBlockState(pos.south()).getBlock() == this;}
        boolean cornerNorthWest = north && west && world.getBlockState(pos.north().west()).getBlock() != this;
        boolean cornerNorthEast = north && east && world.getBlockState(pos.north().east()).getBlock() != this;
        boolean cornerSouthEast = south && east && world.getBlockState(pos.south().east()).getBlock() != this;
        boolean cornerSouthWest = south && west && world.getBlockState(pos.south().west()).getBlock() != this;


        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(CORNER_NORTH_WEST, cornerNorthWest).with(CORNER_NORTH_EAST, cornerNorthEast).with(CORNER_SOUTH_EAST, cornerSouthEast).with(CORNER_SOUTH_WEST, cornerSouthWest);

    }


    private boolean isTable(WorldAccess level, BlockPos pos, Direction checkDirection, Direction tableDirection)
    {
        BlockState state = level.getBlockState(pos.offset(checkDirection));
        return state.getBlock() instanceof BasicTable && state.get(FACING) == tableDirection;
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



    final static VoxelShape table_basic_north = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(2, 0, 0, 4, 2, 12), createCuboidShape(12, 0, 0, 14, 2, 12), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape table_basic_east = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 11,4, 14, 13), createCuboidShape(12, 0, 11, 14, 14, 13), createCuboidShape(4, 0,11, 12, 2, 13));
    final static VoxelShape table_basic = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(2, 0, 2, 4, 14, 4), createCuboidShape(2, 0, 4, 4, 2, 12), createCuboidShape(12, 0, 4, 14, 2, 12), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape table_basic_north_west = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 3, 14, 14, 5), createCuboidShape(0, 0, 3, 12, 2, 5));
    final static VoxelShape table_basic_north_east = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(3, 0, 12, 5, 14, 14), createCuboidShape(3, 0, 0, 5, 2, 12));
    final static VoxelShape table_basic_middle = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16));
    final static VoxelShape table_basic_north_south = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 0, 4, 2, 16), createCuboidShape(12, 0, 0, 14, 2, 16));
    final static VoxelShape table_basic_leg = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(0, 0, 11, 16, 2, 13));
    final static VoxelShape table_basic_double_corner = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(12, 0, 0, 14, 14, 2), createCuboidShape(2, 0, 0, 4, 14, 2));
    final static VoxelShape table_basic_corner_north_west = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 0, 14, 2, 12), createCuboidShape(2, 0, 0, 4, 14, 2));
    final static VoxelShape table_basic_corner_north_east = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 12, 4, 14, 14), createCuboidShape(2, 0, 0, 4, 2, 12), createCuboidShape(12, 0, 0, 14, 14, 2));
    final static VoxelShape table_basic_corner = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 0, 4, 14, 2), createCuboidShape(12, 0, 0, 14, 14, 2));
    final static VoxelShape table_basic_corner_m = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 3, 4, 14, 5), createCuboidShape(12, 0, 3, 14, 14, 5), createCuboidShape(14, 0, 3, 16, 2, 5), createCuboidShape(0, 0, 3, 2, 2, 5));
    final static VoxelShape table_basic_corner_tri = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(11, 0, 0, 13, 14, 2));
    final static VoxelShape table_basic_corner_tri_west = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(2, 0, 14, 4, 14, 16), createCuboidShape(12, 0, 14, 14, 14, 16), createCuboidShape(3, 0, 0, 5, 14, 2));

    final static VoxelShape table_basic_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic);
    final static VoxelShape table_basic_east_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_east);
    final static VoxelShape table_basic_north_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_north);
    final static VoxelShape table_basic_north_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_north);
    final static VoxelShape table_basic_east_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_east);
    final static VoxelShape table_basic_east_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_east);
    final static VoxelShape table_basic_north_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_north);
    final static VoxelShape table_basic_north_west_facing_east = rotateShape(Direction.WEST, Direction.EAST, table_basic_north_west);
    final static VoxelShape table_basic_north_west_north_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_north_west);
    final static VoxelShape table_basic_north_east_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_north_east);
    final static VoxelShape table_basic_north_west_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_north_west);
    final static VoxelShape table_basic_north_east_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_north_east);
    final static VoxelShape table_basic_north_east_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_north_east);
    final static VoxelShape table_basic_north_south_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_north_south);
    final static VoxelShape table_basic_leg_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_leg);
    final static VoxelShape table_basic_leg_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_leg);
    final static VoxelShape table_basic_leg_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_leg);
    final static VoxelShape table_basic_double_corner_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_double_corner);
    final static VoxelShape table_basic_corner_north_east_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_corner_north_east);
    final static VoxelShape table_basic_corner_north_west_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_corner_north_west);
    final static VoxelShape table_basic_corner_north_west_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_corner_north_west);
    final static VoxelShape table_basic_corner_north_east_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_corner_north_east);
    final static VoxelShape table_basic_corner_north_east_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_corner_north_east);
    final static VoxelShape table_basic_corner_north_west_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_corner_north_west);
    final static VoxelShape table_basic_corner_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_corner);
    final static VoxelShape table_basic_corner_m_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_corner_m);
    final static VoxelShape table_basic_corner_m_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_corner_m);
    final static VoxelShape table_basic_corner_facing_east = rotateShape(Direction.NORTH, Direction.EAST, table_basic_corner);
    final static VoxelShape table_basic_corner_m_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_corner_m);
    final static VoxelShape table_basic_corner_facing_west = rotateShape(Direction.NORTH, Direction.WEST, table_basic_corner);
    final static VoxelShape table_basic_corner_tri_west_facing_south = rotateShape(Direction.NORTH, Direction.SOUTH, table_basic_corner_tri_west);

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
            return table_basic;
        } else if (dirWestOrEast && !(north || south || west || east)) {
            return table_basic_facing_east;
        }

        if (dirNorthOrSouth && north && !(south || west || east)) {
            return table_basic_north;
        } else if (dirWestOrEast && north && !(south || west || east)) {
            return table_basic_east;
        }
        if (dirNorthOrSouth && east && !(south || west || north)) {
            return table_basic_east_facing_east;
        } else if (dirWestOrEast && east && !(south || west || north)) {
            return table_basic_north_facing_east;
        }
        if (dirNorthOrSouth && south && !(west || east || north)) {
            return table_basic_north_facing_south;
        } else if (dirWestOrEast && south && !(west || east || north)) {
            return table_basic_east_facing_south;
        }
        if (dirNorthOrSouth && west && !(south || east || north)) {
            return table_basic_east_facing_west;
        } else if (dirWestOrEast && west && !(south || east || north)) {
            return table_basic_north_facing_west;
        }


        if (dirNorthOrSouth && (east && north) && !(south || west || cornerNorthEast)) {
            return table_basic_north_east;
        } else if (dirWestOrEast && (east && north) && !(south || west || cornerNorthEast)) {
            return table_basic_north_west_facing_east;
        }
        if (dirNorthOrSouth && (west && north) && !(south || east || cornerNorthWest)) {
            return table_basic_north_west_north_facing_east;
        } else if (dirWestOrEast && (west && north) && !(south || east || cornerNorthWest)) {
            return table_basic_north_east_facing_west;
        }
        if (dirNorthOrSouth && (south && east) && !(west || north || cornerSouthEast)) {
            return table_basic_north_west_facing_west;
        } else if (dirWestOrEast && (south && east) && !(west || north || cornerSouthEast)) {
            return table_basic_north_east_facing_east;
        }
        if (dirNorthOrSouth && (south && west) && !(east || north || cornerSouthWest)) {
            return table_basic_north_east_facing_south;
        } else if (dirWestOrEast && (south && west) && !(east || north || cornerSouthWest)) {
            return table_basic_north_west;
        }

        if (dirNorthOrSouth && (south && north) && !(east || west)) {
            return table_basic_north_south;
        } else if (dirWestOrEast && (south && north) && !(east || west)) {
            return table_basic_middle;
        }
        if (dirNorthOrSouth && (east && west) && !(north || south)) {
            return table_basic_middle;
        } else if (dirWestOrEast && (east && west) && !(north || south)) {
            return table_basic_north_south_facing_east;
        }

        if (dirNorthOrSouth && (north && west && south) && !(east || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_leg_facing_west;
        } else if (dirWestOrEast && (north && west && south) && !(east || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_middle ;
        }
        if (dirNorthOrSouth && (north && east && south) && !(west || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_leg_facing_east;
        } else if (dirWestOrEast && (north && east && south) && !(west || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_middle ;
        }
        if (dirNorthOrSouth && (north && east && west) && !(south || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_middle;
        } else if (dirWestOrEast && (north && east && west) && !(south || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_leg;
        }
        if (dirNorthOrSouth && (south && east && west) && !( north || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_middle;
        } else if (dirWestOrEast && (south && east && west) && !(north || cornerNorthEast ||cornerNorthWest || cornerSouthEast || cornerSouthWest)) {
            return table_basic_leg_facing_south;
        }

        if (dirNorthOrSouth && (north && east && west && cornerNorthWest && cornerNorthEast) && !( south || cornerSouthEast || cornerSouthWest)) {
            return table_basic_double_corner;
        } else if (dirWestOrEast && (north && east && west && cornerNorthWest && cornerNorthEast) && !( south || cornerSouthEast || cornerSouthWest)) {
            return table_basic_double_corner_facing_east;
        }
        if (dirNorthOrSouth && (south && east && west && cornerSouthEast && cornerSouthWest) && !( north || cornerNorthEast || cornerNorthWest)) {
            return table_basic_double_corner;
        } else if (dirWestOrEast && (south && east && west && cornerSouthEast && cornerSouthWest) && !( north || cornerNorthEast || cornerNorthWest)) {
            return table_basic_double_corner_facing_east;
        }
        if (dirNorthOrSouth && (south && east && north && cornerNorthEast && cornerSouthEast) && !( west || cornerNorthWest || cornerSouthWest)) {
            return table_basic_double_corner;
        } else if (dirWestOrEast && (south && east && north && cornerNorthEast && cornerSouthEast) && !( west  || cornerNorthWest || cornerSouthWest)) {
            return table_basic_double_corner_facing_east;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && cornerSouthWest) && !( east || cornerSouthEast || cornerNorthEast)) {
            return table_basic_double_corner;
        } else if (dirWestOrEast && (south && west && north && cornerNorthWest && cornerSouthWest) && !( east || cornerSouthEast || cornerNorthEast)) {
            return table_basic_double_corner_facing_east;
        }

        if ((south && west && north && east) && !(cornerSouthWest || cornerNorthWest || cornerSouthEast || cornerNorthEast)) {
            return table_basic_middle;}

        if (dirNorthOrSouth && cornerNorthWest && !(cornerSouthWest || cornerSouthEast || cornerNorthEast)) {
            return table_basic_corner_north_west;
        } else if (dirWestOrEast && cornerNorthWest && !(cornerSouthWest || cornerSouthEast || cornerNorthEast)) {
            return table_basic_corner_north_east_facing_west;
        }

        if (dirNorthOrSouth && cornerNorthEast  && !(cornerSouthWest || cornerSouthEast || cornerNorthWest)) {
            return table_basic_corner_north_east;
        } else if (dirWestOrEast && cornerNorthEast && !(cornerSouthWest || cornerSouthEast || cornerNorthWest)) {
            return table_basic_corner_north_west_facing_east;
        }

        if (dirNorthOrSouth && cornerSouthEast  && !(cornerSouthWest || cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_north_west_facing_south;
        } else if (dirWestOrEast && cornerSouthEast && !(cornerSouthWest || cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_north_east_facing_east;
        }
        if (dirNorthOrSouth && cornerSouthWest  && !(cornerSouthEast || cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_north_east_facing_south;
        } else if (dirWestOrEast && cornerSouthWest && !(cornerSouthEast || cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_north_west_facing_west;
        }

        if (dirNorthOrSouth && south && west && north && cornerNorthWest && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) {
            return table_basic_double_corner;}
           else if (dirWestOrEast && south && west && north && cornerNorthWest && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) {
            return table_basic_double_corner_facing_east;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerNorthEast) && !(cornerSouthEast || cornerSouthWest)) {
            return table_basic_corner;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && cornerNorthEast && east) && !(cornerSouthEast || cornerSouthWest)) {
            return table_basic_corner_m;
        }
        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerSouthEast) && !(cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_facing_south;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && cornerSouthEast && east) && !(cornerNorthEast || cornerNorthWest)) {
            return table_basic_corner_m_facing_south;
        }
        if (dirNorthOrSouth && (south && west && north && cornerNorthEast && east && cornerSouthEast) && !(cornerSouthWest || cornerNorthWest)) {
            return table_basic_corner_m_facing_east;}
        else if (dirWestOrEast && (south && west && north && cornerNorthEast && cornerSouthEast && east) && !(cornerSouthWest || cornerNorthWest)) {
            return table_basic_corner_facing_east;
        }
        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerNorthWest) && !(cornerSouthEast || cornerNorthEast)) {
            return table_basic_corner_m_facing_west;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && cornerNorthWest && east) && !(cornerSouthEast || cornerNorthEast)) {
            return table_basic_corner_facing_west;
        }

        if (dirNorthOrSouth && (south && west && north && cornerSouthWest && east && cornerSouthEast && cornerNorthEast) && !cornerNorthWest) {
            return table_basic_corner_tri;}
        else if (dirWestOrEast && (south && west && north && cornerSouthWest && east && cornerNorthEast && cornerSouthEast) && !cornerNorthWest) {
            return table_basic_corner_facing_west;
        }

        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthEast && cornerNorthEast) && !cornerSouthWest) {
            return table_basic_corner_tri_west_facing_south;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerNorthEast && cornerSouthEast) && !cornerSouthWest) {
            return table_basic_corner_facing_west;
        }

        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthEast) && !(cornerNorthEast || cornerSouthWest)) {
            return table_basic_corner_tri_west_facing_south;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerSouthEast) && !(cornerSouthWest || cornerNorthEast)) {
            return table_basic_corner_facing_west;
        }

        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthWest && cornerSouthEast) && !cornerNorthEast) {
            return table_basic_corner_tri_west_facing_south;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerSouthEast && cornerSouthWest) && !cornerNorthEast) {
            return table_basic_corner_facing_west;
        }

        if (dirNorthOrSouth && (south && west && north && cornerNorthWest && east && cornerSouthWest && cornerNorthEast) && !cornerSouthEast) {
            return table_basic_corner_tri_west_facing_south;}
        else if (dirWestOrEast && (south && west && north && cornerNorthWest && east && cornerNorthEast && cornerSouthWest) && !cornerSouthEast) {
            return table_basic_corner_facing_west;
        }


        if (dirNorthOrSouth && (south && west && north && east && cornerSouthWest && cornerNorthEast) && !(cornerSouthEast || cornerNorthWest)) {
            return table_basic_corner_tri_west_facing_south;}
        else if (dirWestOrEast && (south && west && north && east && cornerSouthWest && cornerNorthEast) && !(cornerSouthEast || cornerNorthWest)) {
            return table_basic_corner_facing_west;
        }

        else
        {
            return VoxelShapes.fullCube();
        }



        }
    }


