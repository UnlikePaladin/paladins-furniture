package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
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
public class LogTableBlock extends HorizontalDirectionalBlock {
    private final Block baseBlock;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> WOOD_LOG_TABLES = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_NATURAL_TABLES = new ArrayList<>();
    public static final MapCodec<LogTableBlock> CODEC = simpleCodec(LogTableBlock::new);
    public LogTableBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(LogTableBlock.class)){
            WOOD_LOG_TABLES.add(new FurnitureBlock(this, "table_"));
        }
        else if (this.getClass().isAssignableFrom(LogTableBlock.class)){
            STONE_NATURAL_TABLES.add(new FurnitureBlock(this, "natural_table"));
        }
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamWoodLogTables() {
        return WOOD_LOG_TABLES.stream();
    }
    public static Stream<FurnitureBlock> streamStoneNaturalTables() {
        return STONE_NATURAL_TABLES.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.is(state.getBlock())) {
            this.baseBlockState.neighborChanged(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onPlace(this.baseBlockState, world, pos, oldState, false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }


    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
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
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    public boolean canConnect(BlockState blockState)
    {
        return PaladinFurnitureMod.getPFMConfig().doTablesOfDifferentMaterialsConnect() ? blockState.getBlock() instanceof LogTableBlock : blockState.getBlock() == this;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    final static VoxelShape LOG_TABLE = Shapes.or(box(0, 14, 0, 16, 16, 16), box(2, 0, 5, 4.5, 14, 11), box(11.5, 0, 5, 14, 14, 11));
    final static VoxelShape LOG_TABLE_MIDDLE = Shapes.or(box(0, 14, 0, 16, 16, 16));
    final static VoxelShape LOG_TABLE_ONE = Shapes.or(box(0, 14, 0, 16, 16, 16), box(6, 0, 5, 8.5, 14, 11));
    final static VoxelShape LOG_TABLE_ONE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, LOG_TABLE_ONE);
    final static VoxelShape LOG_TABLE_ONE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, LOG_TABLE_ONE);
    final static VoxelShape LOG_TABLE_ONE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, LOG_TABLE_ONE);
    final static VoxelShape LOG_TABLE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, LOG_TABLE);
    // Cursed I know
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean left = isTable(view, pos, dir.getCounterClockWise(), dir);
        boolean right = isTable(view, pos, dir.getClockWise(), dir);
        boolean dirNorthOrSouth = dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH);
        boolean dirWestOrEast = dir.equals(Direction.WEST) || dir.equals(Direction.EAST);
        if (left && right) {
            return LOG_TABLE_MIDDLE;
        }
        else if (left) {
            if (dirNorthOrSouth) {
                return LOG_TABLE_ONE;}
            else if (dirWestOrEast) {
                return LOG_TABLE_ONE_WEST;}
            else {
                return LOG_TABLE;
            }
        }
        else if (right) {
            if (dirNorthOrSouth) {
                return LOG_TABLE_ONE_SOUTH;}
            else if (dirWestOrEast) {
                return LOG_TABLE_ONE_EAST;}
            else {
                return LOG_TABLE;
            }
        }
        else {
            if (dirWestOrEast) {
                return LOG_TABLE_EAST;}
            else {
                return LOG_TABLE;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}


