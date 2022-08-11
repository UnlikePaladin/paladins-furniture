package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import com.unlikepaladin.pfm.registry.StatisticsRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.Optional;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class Stove extends SmokerBlock implements Waterloggable {
    private static final List<FurnitureBlock> STOVES = new ArrayList<>();
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public Stove(Settings settings) {
        super(settings);
        if (this.getClass().isAssignableFrom(Stove.class)){
            STOVES.add(new FurnitureBlock(this, "stove"));
        }
        setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false).with(FACING, Direction.NORTH).with(LIT, false));
    }
    public static Stream<FurnitureBlock> streamStoves() {
        return STOVES.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, WATERLOGGED);
    }
    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            player.incrementStat(StatisticsRegistry.STOVE_OPENED);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (!player.isSneaking()) {
            ItemStack itemStack;
            StoveBlockEntity stoveBlockEntity;
            Optional<CampfireCookingRecipe> optional;
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StoveBlockEntity && (optional = (stoveBlockEntity = (StoveBlockEntity)blockEntity).getRecipeFor(itemStack = player.getStackInHand(hand))).isPresent()) {
                if (stoveBlockEntity.addItem(player.getAbilities().creativeMode ? itemStack.copy() : itemStack, optional.get().getCookTime())) {
                    player.incrementStat(StatisticsRegistry.STOVE_OPENED);
                    return ActionResult.SUCCESS;
                }
            }
            if(blockEntity instanceof StoveBlockEntity){
                stoveBlockEntity = (StoveBlockEntity)blockEntity;
                for (int i = 0; i < stoveBlockEntity.getItemsBeingCooked().size(); i++) {
                    ItemStack stack = stoveBlockEntity.getItemsBeingCooked().get(i);
                    if (stack.isEmpty()) continue;
                    if(world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(stack), world).isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, stoveBlockEntity.removeStack(i));
                        world.spawnEntity(itemEntity);
                        player.incrementStat(StatisticsRegistry.STOVE_OPENED);
                        return ActionResult.SUCCESS;
                    }
                }
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        }
        else{
            this.openScreen(world, pos, player);
        }
        return ActionResult.CONSUME;
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    protected static final VoxelShape STOVE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 14),createCuboidShape(0, 1, 14, 16, 16, 15),createCuboidShape(1.8, 12.2, 15.5375, 14.3, 12.799, 16.1375),createCuboidShape(2.5, 12.2, 14.07, 3.1, 12.79, 15.56),createCuboidShape(12.6, 12.2, 14.07, 13.2, 12.79, 15.57),createCuboidShape(1.8, 2.89, 15.437, 14.3, 3.49, 16.037),createCuboidShape(2.5, 2.89, 13.47, 3.1, 3.49, 15.47),createCuboidShape(12.6, 2.89, 13.47, 13.2, 3.49, 15.47),createCuboidShape(0, 16, 0, 16, 19, 1));
    protected static final VoxelShape STOVE_NORTH = rotateShape(Direction.NORTH, Direction.SOUTH, STOVE);
    protected static final VoxelShape STOVE_WEST = rotateShape(Direction.NORTH, Direction.WEST, STOVE);
    protected static final VoxelShape STOVE_EAST = rotateShape(Direction.NORTH, Direction.EAST, STOVE);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> STOVE_EAST;
            case NORTH -> STOVE_NORTH;
            case SOUTH -> STOVE;
            default -> STOVE_WEST;
        };
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(LIT)) {
            return;
        }
        double x = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double z = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            world.playSound(x, y, z, SoundEvents.BLOCK_SMOKER_SMOKE, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StoveBlockEntity(BlockEntityRegistry.STOVE_BLOCK_ENTITY, pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
                return checkType(type, BlockEntityRegistry.STOVE_BLOCK_ENTITY, StoveBlockEntity::clientTick);
        } else {
                return checkType(type, BlockEntityRegistry.STOVE_BLOCK_ENTITY, StoveBlockEntity::litServerTick);
        }
    }

}
