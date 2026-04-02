package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class InnerTrashcanBlock extends BaseEntityBlock {
    public static final MapCodec<InnerTrashcanBlock> CODEC = createCodec(InnerTrashcanBlock::new);
    public InnerTrashcanBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public static final VoxelShape TRASHCAN = Shapes.or(box(3.5, 11, 3.25, 12.5, 11.5, 12.75), box(3.5, 0, 3.5, 12.5, 11, 12.5));
    public static final VoxelShape TRASHCAN_EAST_WEST = rotateShape(Direction.NORTH, Direction.EAST, TRASHCAN);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        return;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TrashcanBlockEntity) {
            openScreen(player, state, world, pos);
            player.awardStat(Statistics.TRASHCAN_OPENED);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean bl = world.hasNeighborSignal(pos);
        if (bl != state.getValue(POWERED)) {
            if (bl) {
                if (world.getBlockEntity(pos) instanceof TrashcanBlockEntity){
                    TrashcanBlockEntity trashcanBlockEntity = (TrashcanBlockEntity) world.getBlockEntity(pos);
                    trashcanBlockEntity.clearContent();
                }
            }
            world.setBlock(pos, state.setValue(POWERED, bl), 3);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Container) {
            Containers.dropContents(world, pos, (Container) blockEntity);
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
