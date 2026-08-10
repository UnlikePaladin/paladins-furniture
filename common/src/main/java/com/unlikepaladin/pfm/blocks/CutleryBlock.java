package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CutleryBlock extends HorizontalDirectionalBlock {
    private static final List<FurnitureBlock> CUTLERY = new ArrayList<>();
    public static final MapCodec<CutleryBlock> CODEC = simpleCodec(CutleryBlock::new);

    public CutleryBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
        CUTLERY.add(new FurnitureBlock(this, "cutlery"));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamCutlery() {
        return CUTLERY.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (!state.canSurvive(levelReader, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    private static final VoxelShape FACING_NORTH = Shapes.or(box(3, 0, 0,11, 0.5, 15.5));
    private static final VoxelShape FACING_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, FACING_NORTH);
    private static final VoxelShape FACING_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, FACING_NORTH);
    private static final VoxelShape FACING_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, FACING_NORTH);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        switch (dir) {
            case WEST: {
                return FACING_SOUTH;
            }
            case NORTH: {
                return FACING_WEST;
            }
            case SOUTH: {
                return FACING_EAST;
            }
            default: {
                return FACING_NORTH;
            }
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Block block = (BuiltInRegistries.BLOCK.getValue(BuiltInRegistries.ITEM.getKey(itemStack.getItem())));
        if(block instanceof PlateBlock) {
            BlockState newState = block.defaultBlockState();
            world.setBlockAndUpdate(pos, newState.setValue(PlateBlock.CUTLERY, true).setValue(FACING, state.getValue(FACING)));
            if (!player.isCreative())
                itemStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = Direction.DOWN;
        return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
    }
}
