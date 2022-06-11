package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.menus.WorkbenchScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
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
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.ClassicStool.rotateShape;

public class WorkingTable extends HorizontalFacingBlock {
    public WorkingTable(Settings settings) {
        super(settings);
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new WorkbenchScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }
}
