package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ClassicTable extends Block {

    private final Block baseBlock;
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");


    private final BlockState baseBlockState;
    public ClassicTable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(NORTH, false).with(SOUTH,false).with(EAST,false).with(WEST,false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }
    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(NORTH);
        stateManager.add(EAST);
        stateManager.add(WEST);
        stateManager.add(SOUTH);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }



    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? getShape(state, world, pos) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        BlockState blockState = this.getDefaultState();
        return getShape(blockState, world, blockPos);
    }


    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        boolean north = false;
        boolean east = false;
        boolean west = false;
        boolean south = false;
      if (world.getBlockState(pos.north()).getBlock() == this){
          north = world.getBlockState(pos.north()).getBlock() == this;}

      if (world.getBlockState(pos.east()).getBlock() == this){
          east = world.getBlockState(pos.east()).getBlock() == this;}

      if (world.getBlockState(pos.west()).getBlock() == this){
            west = world.getBlockState(pos.west()).getBlock() == this;}

      if (world.getBlockState(pos.south()).getBlock() == this){
            south = world.getBlockState(pos.south()).getBlock() == this;}

        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);

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



    final static VoxelShape table_classic = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(2, 0, 2, 4, 14, 4), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape table_classic_middle = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16));
    final static VoxelShape table_classic_two = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 0, 12,15, 15, 14), createCuboidShape(13, 0, 2, 15, 15, 4));
    final static VoxelShape table_classic_one = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 0, 13, 15, 15, 15));

    //Cursed I know
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {

        boolean north = state.get(NORTH);
        boolean east = state.get(EAST);
        boolean west = state.get(WEST);
        boolean south = state.get(SOUTH);

        if ( !(north || south || west || east)) {
            return table_classic;
        }
            if (south && west && !(north || east)) {
                return rotateShape(Direction.NORTH, Direction.WEST, table_classic_one);
            }
                if (east && west && south && !north) {
                    return rotateShape(Direction.NORTH, Direction.SOUTH, table_classic_middle);
                }
                     if (north && west && south && !east) {
                        return table_classic_middle;
                    }

                     if (north && west && east && !south) {
                            return table_classic_middle;
                        }

                    if (north && south && east && !west) {
                            return table_classic_middle;
                        }

                    if ((north && west) && !(east || south)) {
                            return table_classic_one;
                        }

                 if ((east && south) && !(west || north)) {
                     return rotateShape(Direction.NORTH, Direction.SOUTH, table_classic_one);
                }
        if ((east && north) && !(west || south)) {
            return rotateShape(Direction.NORTH, Direction.EAST, table_classic_one);
        }
        if (east && north && west && south) {
            return table_classic_middle;
        }
        if ((east && west) && !(north || south)) {
            return table_classic_middle;
        }
        if ((north && south) && !(east || west)) {
            return table_classic_middle;
        }
        if (east && !(north || south || west)) {
            return rotateShape(Direction.NORTH, Direction.SOUTH, table_classic_two);
        }

        if (west && !(north || south || east)) {
            return table_classic_two;
        }

        if (south && !(north || west || east)) {
            return rotateShape(Direction.NORTH, Direction.WEST, table_classic_two);
        }
        if (north && !(south || west || east)) {
            return rotateShape(Direction.NORTH, Direction.EAST, table_classic_two);
        }
        else {
                            return VoxelShapes.fullCube();
                        }


    }
}


