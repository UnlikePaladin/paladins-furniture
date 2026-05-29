package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import com.unlikepaladin.pfm.items.ShowerHandleItem;
import com.unlikepaladin.pfm.registry.BlockEntities;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class BasicShowerHeadBlock extends HorizontalFacingBlockWithEntity {
    public BasicShowerHeadBlock(Properties settings) {
        super(settings);
    }

    public static final MapCodec<BasicShowerHeadBlock> CODEC = simpleCodec(BasicShowerHeadBlock::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
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
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING) && !state.canSurvive(levelReader, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return  super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
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
        return switch (state.getValue(FACING)){
            case NORTH -> SHOWER_HEAD_NORTH;
            case WEST -> SHOWER_HEAD_WEST;
            case EAST -> SHOWER_HEAD_EAST;
            default -> SHOWER_HEAD_SOUTH;
        };
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ShowerHandleItem)
            return InteractionResult.PASS;

        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }


    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof ShowerHeadBlockEntity) {
            ShowerHeadBlockEntity showerHeadBlockEntity = (ShowerHeadBlockEntity) world.getBlockEntity(pos);
            showerHeadBlockEntity.setOpen(!showerHeadBlockEntity.isOpen());
            world.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntities.SHOWER_HEAD_BLOCK_ENTITY, ShowerHeadBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
