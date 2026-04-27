package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DinnerTableBlock extends HorizontalDirectionalBlock  {

    private final Block baseBlock;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final BlockState baseBlockState;

    private static final List<FurnitureBlock> WOOD_DINNER_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DINNER_TABLES = new ArrayList<>();
    public DinnerTableBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(DinnerTableBlock.class)){
            WOOD_DINNER_TABLES.add(new FurnitureBlock(this, "table_dinner"));
        }
        else if (this.getClass().isAssignableFrom(DinnerTableBlock.class)){
            STONE_DINNER_TABLES.add(new FurnitureBlock(this, "table_dinner"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodDinnerTables() {
        return WOOD_DINNER_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneDinnerTables() {
        return STONE_DINNER_TABLES.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }


    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof DinnerTableBlock : blockState.getBlock() == this;
    }

    public boolean isTable(BlockGetter world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.relative(direction));
        if(canConnect(state))
        {
            Direction sourceDirection = state.getValue(FACING);
            return sourceDirection.equals(tableDirection);
        }
        return false;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    /**
     * Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    final static VoxelShape dinner_table = Shapes.or(box(0, 14, 0, 16, 16, 16), box(0.1, 0, 2, 15.8, 14, 4.05), box(0.1, 0, 11.9, 15.8, 14, 13.95));
    final static VoxelShape dinner_table_middle = Shapes.or(box(0, 14, 0, 16, 16, 16));
    final static VoxelShape dinner_table_one_east = Shapes.or(box(0, 14, 0, 16, 16, 16), box(0.1, 0, 2, 15.8, 14, 4.05));
    final static VoxelShape dinner_table_one_south = rotateShape(Direction.NORTH, Direction.WEST, dinner_table_one_east);
    final static VoxelShape dinner_table_one = rotateShape(Direction.NORTH, Direction.EAST, dinner_table_one_east);
    final static VoxelShape dinner_table_one_west = rotateShape(Direction.NORTH, Direction.SOUTH, dinner_table_one_east);
    final static VoxelShape dinner_table_east = rotateShape(Direction.NORTH, Direction.EAST, dinner_table);

    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean dirNorthOrSouth = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
        boolean dirWestOrEast = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);
        boolean left = isTable(view, pos, dir.getCounterClockWise(), dir);
        boolean right = isTable(view, pos, dir.getClockWise(), dir);

        if (left && right) {
            return dinner_table_middle;
        }
        else if (left) {
            if (dirNorthOrSouth) {
                return dinner_table_one;}
            else if (dirWestOrEast) {
                return dinner_table_one_east;}
            else {
                return dinner_table;
            }
        }
        else if (right) {
            if (dirNorthOrSouth) {
                return dinner_table_one_south;}
            else if (dirWestOrEast) {
                return dinner_table_one_west;}
            else {
                return dinner_table;
            }
        }
        else {
            if (dirWestOrEast) {
                return dinner_table;}
            else {
                return dinner_table_east;
            }
        }
    }
}


