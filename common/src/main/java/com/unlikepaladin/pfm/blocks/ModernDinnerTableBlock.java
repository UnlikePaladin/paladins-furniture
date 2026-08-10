package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModernDinnerTableBlock extends Block {
    private final Block baseBlock;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private static final List<FurnitureBlock> WOOD_DINNER_MODERN_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DINNER_MODERN_TABLES = new ArrayList<>();
    private final BlockState baseBlockState;

    public ModernDinnerTableBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.X));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ModernDinnerTableBlock.class)){
            WOOD_DINNER_MODERN_TABLES.add(new FurnitureBlock(this, "table_modern_dinner"));
        }
        else if (this.getClass().isAssignableFrom(ModernDinnerTableBlock.class)){
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

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(AXIS, ctx.getHorizontalDirection().getClockWise().getAxis());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof ModernDinnerTableBlock : blockState.getBlock() == this;
    }

    public boolean isTable(BlockGetter world, BlockPos pos, Direction.Axis direction, int i)
    {
        BlockState state = world.getBlockState(pos.relative(direction, i));
        if(canConnect(state))
        {
            Direction.Axis sourceDirection = state.getValue(AXIS);
            return sourceDirection.equals(direction);
        }
        return false;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    final static VoxelShape MODERN_DINNER_TABLE = Shapes.or(box(0, 14, 0, 16, 16, 16), box(12, 0, 12, 14, 14, 14), box(12, 0, 2, 14, 14, 4), box(13, 2, 7,15, 14, 9), box(1, 2, 7, 3, 14, 9),box(2, 0, 2,4, 14, 4),box(2, 0, 4, 4, 2, 12), box(3, 2, 7,13, 4, 9),box(12, 0, 4,14, 2, 12), box(2, 0, 12,4, 14, 14));
    final static VoxelShape MODERN_DINNER_TABLE_MIDDLE = Shapes.or(box(0, 14, 0, 16, 16, 16),box(0, 2, 7,16, 4, 9 ));
    final static VoxelShape MODERN_DINNER_TABLE_ONE = Shapes.or(box(0, 14, 0, 16, 16, 16), box(13, 2, 7, 15, 14, 9), box(12, 0, 12,14, 14, 14), box(12, 0, 4,14, 2, 12 ), box(0, 2, 7,13, 4, 9), box(12, 0, 2,14, 14, 4 ));
    final static VoxelShape MODERN_DINNER_TABLE_ONE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_ONE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_ONE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE_ONE);
    final static VoxelShape MODERN_DINNER_TABLE_MIDDLE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE_MIDDLE);
    final static VoxelShape MODERN_DINNER_TABLE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, MODERN_DINNER_TABLE);

    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction.Axis dir = state.getValue(AXIS);
        boolean dirNorthOrSouth = dir.equals(Direction.Axis.X);
        boolean dirWestOrEast = dir.equals(Direction.Axis.Z);
        boolean left = isTable(view, pos, dir, -1);
        boolean right = isTable(view, pos, dir, 1);

        if (left && right) {
            if (dirNorthOrSouth) {
                return MODERN_DINNER_TABLE_MIDDLE;
            } else {
                return MODERN_DINNER_TABLE_MIDDLE_EAST;
            }
        }
        else if (right) {
            if (dirNorthOrSouth) {
                return MODERN_DINNER_TABLE_ONE_SOUTH;
            } else {
                return MODERN_DINNER_TABLE_ONE_WEST;
            }
        }
        else if (left) {
            if (dirNorthOrSouth) {
                return MODERN_DINNER_TABLE_ONE;
            } else {
                return MODERN_DINNER_TABLE_ONE_EAST;
            }
        }
        else {
            if (dirWestOrEast) {
                return MODERN_DINNER_TABLE_EAST;}
            else {
                return MODERN_DINNER_TABLE;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}


