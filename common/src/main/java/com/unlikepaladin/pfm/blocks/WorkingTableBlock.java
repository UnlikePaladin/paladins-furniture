package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.ClassicStoolBlock.rotateShape;

public class WorkingTableBlock extends HorizontalFacingBlock implements Waterloggable {
    private static final List<WorkingTableBlock> WORKING_TABLES = new ArrayList<>();
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public WorkingTableBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.NORTH));
        WORKING_TABLES.add(this);
    }
    public static Stream<WorkingTableBlock> streamWorkingTables() {
        return WORKING_TABLES.stream();
    }

    private static final Text TITLE = new TranslatableText("container.pfm.working_table");
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        //player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return ActionResult.CONSUME;
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public static VoxelShape WORKTABLE_SHAPE = VoxelShapes.union(createCuboidShape(0, 14, 0, 16,16,16), createCuboidShape(2, 1, 2,14, 14, 14),createCuboidShape(1.5, 0, 1,4.5, 1, 15),createCuboidShape(11.5, 0, 1,14.5, 1, 15),createCuboidShape(0, 16, 14,16, 18, 16),createCuboidShape(0, 16, 12,1, 17, 14),createCuboidShape(15, 16, 12,16, 17, 14));
    public static VoxelShape WORKTABLE_SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, WORKTABLE_SHAPE);
    public static VoxelShape WORKTABLE_SHAPE_EAST = rotateShape(Direction.NORTH, Direction.EAST, WORKTABLE_SHAPE);
    public static VoxelShape WORKTABLE_SHAPE_WEST = rotateShape(Direction.NORTH, Direction.WEST, WORKTABLE_SHAPE);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(FACING)) {
            case NORTH -> {return WORKTABLE_SHAPE_SOUTH;}
            case SOUTH -> {return WORKTABLE_SHAPE;}
            case WEST -> {return WORKTABLE_SHAPE_EAST;}
            default -> {return WORKTABLE_SHAPE_WEST;}
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new WorkbenchScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
