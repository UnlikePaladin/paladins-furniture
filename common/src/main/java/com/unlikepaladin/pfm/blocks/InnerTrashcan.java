package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
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

import static com.unlikepaladin.pfm.blocks.SimpleStool.rotateShape;

public class InnerTrashcan extends BlockWithEntity {
    public InnerTrashcan(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }
    protected static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static final VoxelShape TRASHCAN = VoxelShapes.union(createCuboidShape(4.5, 0, 2, 11.5, 11, 14), createCuboidShape(4.5, 11, 1.75, 11.5, 11.5, 14.25));
    public static final VoxelShape TRASHCAN_EAST_WEST = rotateShape(Direction.NORTH, Direction.EAST, TRASHCAN);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case SOUTH:
            case NORTH: {
                return TRASHCAN;
            }
            case WEST:
            case EAST: {
                return TRASHCAN_EAST_WEST;
            }
        }
        return TRASHCAN;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openScreen(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        return;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TrashcanBlockEntity) {
            openScreen(player, state, world, pos);
            player.incrementStat(Statistics.TRASHCAN_OPENED);
        }
        return ActionResult.CONSUME;
    }

}
