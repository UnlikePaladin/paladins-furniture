package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassicTableBlock extends Block {
    private final Block baseBlock;
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    private static final List<FurnitureBlock> WOOD_CLASSIC_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_TABLES = new ArrayList<>();
    private final BlockState baseBlockState;
    public ClassicTableBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(NORTH, false).with(SOUTH,false).with(EAST,false).with(WEST,false));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicTableBlock.class)){
            WOOD_CLASSIC_TABLES.add(new FurnitureBlock(this, "table_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicTableBlock.class)){
            STONE_CLASSIC_TABLES.add(new FurnitureBlock(this, "table_classic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicTables() {
        return WOOD_CLASSIC_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicTables() {
        return STONE_CLASSIC_TABLES.stream();
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

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
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

    boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof ClassicTableBlock : blockState.getBlock() == this;
    }

    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        boolean north = false;
        boolean east = false;
        boolean west = false;
        boolean south = false;
          if (canConnect(world.getBlockState(pos.north()))){
              north = canConnect(world.getBlockState(pos.north()));}

          if (canConnect(world.getBlockState(pos.east()))){
              east = canConnect(world.getBlockState(pos.east()));}

          if (canConnect(world.getBlockState(pos.west()))){
              west = canConnect(world.getBlockState(pos.west()));}

          if (canConnect(world.getBlockState(pos.south()))){
              south = canConnect(world.getBlockState(pos.south()));}

        return state.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
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



    final static VoxelShape TABLE_CLASSIC = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(12, 0, 12, 14, 14, 14), createCuboidShape(12, 0, 2, 14, 14, 4), createCuboidShape(2, 0, 2, 4, 14, 4), createCuboidShape(2, 0, 12, 4, 14, 14));
    final static VoxelShape TABLE_CLASSIC_MIDDLE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16));
    final static VoxelShape TABLE_CLASSIC_TWO = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 0, 12,15, 15, 14), createCuboidShape(13, 0, 2, 15, 15, 4));
    final static VoxelShape TABLE_CLASSIC_ONE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16, 16, 16), createCuboidShape(13, 0, 13, 15, 15, 15));

    final static VoxelShape TABLE_CLASSIC_ONE_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_CLASSIC_ONE);
    final static VoxelShape TABLE_CLASSIC_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_CLASSIC_MIDDLE);
    final static VoxelShape TABLE_CLASSIC_ONE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_CLASSIC_ONE);
    final static VoxelShape TABLE_CLASSIC_ONE_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_CLASSIC_ONE);
    final static VoxelShape TABLE_CLASSIC_TWO_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, TABLE_CLASSIC_TWO);
    final static VoxelShape TABLE_CLASSIC_TWO_WEST = rotateShape(Direction.NORTH, Direction.WEST, TABLE_CLASSIC_TWO);
    final static VoxelShape TABLE_CLASSIC_TWO_EAST = rotateShape(Direction.NORTH, Direction.EAST, TABLE_CLASSIC_TWO);
    //Cursed I know
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {

        boolean north = state.get(NORTH);
        boolean east = state.get(EAST);
        boolean west = state.get(WEST);
        boolean south = state.get(SOUTH);

        if ( !(north || south || west || east)) {
            return TABLE_CLASSIC;
        }
        if (south && west && !(north || east)) {
            return TABLE_CLASSIC_ONE_WEST;
        }
        if (east && west && south && !north) {
            return TABLE_CLASSIC_MIDDLE_SOUTH;
        }
         if (north && west && south && !east) {
            return TABLE_CLASSIC_MIDDLE;
        }

         if (north && west && east && !south) {
            return TABLE_CLASSIC_MIDDLE;
        }

        if (north && south && east && !west) {
            return TABLE_CLASSIC_MIDDLE;
        }

        if ((north && west) && !(east || south)) {
            return TABLE_CLASSIC_ONE;
        }

        if ((east && south) && !(west || north)) {
            return TABLE_CLASSIC_ONE_SOUTH;
        }
        if ((east && north) && !(west || south)) {
            return TABLE_CLASSIC_ONE_EAST;
        }
        if (east && north && west && south) {
            return TABLE_CLASSIC_MIDDLE;
        }
        if ((east && west) && !(north || south)) {
            return TABLE_CLASSIC_MIDDLE;
        }
        if ((north && south) && !(east || west)) {
            return TABLE_CLASSIC_MIDDLE;
        }
        if (east && !(north || south || west)) {
            return TABLE_CLASSIC_TWO_SOUTH;
        }

        if (west && !(north || south || east)) {
            return TABLE_CLASSIC_TWO;
        }

        if (south && !(north || west || east)) {
            return TABLE_CLASSIC_TWO_WEST;
        }
        if (north && !(south || west || east)) {
            return TABLE_CLASSIC_TWO_EAST;
        }
        else
        {
            return VoxelShapes.fullCube();
        }

    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}


