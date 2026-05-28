package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.BasicToiletBlock.createTicketHelper;
import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class KitchenStovetopBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final List<KitchenStovetopBlock> KITCHEN_STOVETOPS = new ArrayList<>();
    public static final MapCodec<KitchenStovetopBlock> CODEC = simpleCodec(KitchenStovetopBlock::new);

    public KitchenStovetopBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, true));
        KITCHEN_STOVETOPS.add(this);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<KitchenStovetopBlock> streamKitchenStovetop() {
        return KITCHEN_STOVETOPS.stream();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Block neighborBlock = world.getBlockState(pos.below()).getBlock();
        return neighborBlock instanceof KitchenCounterOvenBlock || neighborBlock instanceof KitchenCounterBlock;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StovetopBlockEntity stovetopBlockEntity && world.recipeAccess().propertySet(RecipePropertySet.CAMPFIRE_INPUT).test(itemStack)) {
            if (world instanceof ServerLevel serverWorld) {
                if (stovetopBlockEntity.addItem(serverWorld, player, itemStack)) {
                    player.awardStat(Statistics.STOVETOP_USED);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.useItemOn(itemStack, state, world, pos, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        StovetopBlockEntity stovetopBlockEntity;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof StovetopBlockEntity){
            stovetopBlockEntity = (StovetopBlockEntity)blockEntity;
            for (int i = 0; i < stovetopBlockEntity.getItemsBeingCooked().size(); i++) {
                ItemStack stack = stovetopBlockEntity.getItemsBeingCooked().get(i);
                if (stack.isEmpty()) continue;
                if(world instanceof ServerLevel serverWorld && serverWorld.recipeAccess().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), world).isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, stovetopBlockEntity.removeItemNoUpdate(i));
                    world.addFreshEntity(itemEntity);
                    player.awardStat(Statistics.STOVETOP_USED);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }


    @Override
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return direction == Direction.DOWN && !this.canSurvive(state, levelReader, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    protected static final VoxelShape STOVETOP = Shapes.or(box(0, 0, 1, 16, 1, 15));
    protected static final VoxelShape STOVETOP_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, STOVETOP);
    protected static final VoxelShape STOVETOP_EAST = rotateShape(Direction.NORTH, Direction.EAST, STOVETOP);
    protected static final VoxelShape STOVETOP_WEST = rotateShape(Direction.NORTH, Direction.WEST, STOVETOP);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir) {
            case WEST -> STOVETOP_EAST;
            case NORTH -> STOVETOP_SOUTH;
            case SOUTH -> STOVETOP;
            default -> STOVETOP_WEST;
        };
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
    }
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (world.isClientSide) {
            if (state.getValue(LIT)) {
                return createTicketHelper(type, BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntity::clientTick);
            }
        } else {
            if (state.getValue(LIT)) {
                return createTicketHelper(type, BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntity::litServerTick);
            }
            return createTicketHelper(type, BlockEntities.STOVE_TOP_BLOCK_ENTITY, StovetopBlockEntity::unlitServerTick);
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StovetopBlockEntity stovetopBlockEntity) {
            Containers.dropContents(world, pos, stovetopBlockEntity.getContainer());
            world.updateNeighbourForOutputSignal(pos, this);
            stovetopBlockEntity.setRemoved();
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }
}
