package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import com.unlikepaladin.pfm.items.ShowerHandleItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class BasicShowerHeadBlock extends HorizontalFacingBlockWithEntity {
    public BasicShowerHeadBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public static final VoxelShape SHOWER_HEAD_SOUTH = Shapes.or(box(6.5, 6.5, 15, 9.5, 9.5, 16), box(7.5, 7.5, 7,8.5, 8.5, 15), box(7.5, 5.5, 7,8.5, 7.5, 8),box(4.8, 4, 4.2,11.3, 5.5, 10.7));
    public static final VoxelShape SHOWER_HEAD_NORTH = rotateShape(Direction.SOUTH, Direction.NORTH, SHOWER_HEAD_SOUTH);
    public static final VoxelShape SHOWER_HEAD_EAST = rotateShape(Direction.SOUTH, Direction.EAST, SHOWER_HEAD_SOUTH);
    public static final VoxelShape SHOWER_HEAD_WEST = rotateShape(Direction.SOUTH, Direction.WEST, SHOWER_HEAD_SOUTH);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)){
            case NORTH: return SHOWER_HEAD_NORTH;
            case WEST: return SHOWER_HEAD_WEST;
            case EAST: return SHOWER_HEAD_EAST;
            default: return SHOWER_HEAD_SOUTH;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).getItem() instanceof ShowerHandleItem)
            return super.use(state, world, pos, player, hand, hit);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof ShowerHeadBlockEntity) {
            ShowerHeadBlockEntity showerHeadBlockEntity = (ShowerHeadBlockEntity) world.getBlockEntity(pos);
            showerHeadBlockEntity.setOpen(!showerHeadBlockEntity.isOpen());
            world.sendBlockUpdated(pos, state, state, 3);
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockView world) {
        return getBlockEntity();
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity() {
        return null;
    }
}
