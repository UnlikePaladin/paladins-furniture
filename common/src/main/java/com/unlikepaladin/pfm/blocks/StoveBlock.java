package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SmokerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StoveBlock extends SmokerBlock implements DynamicRenderLayerInterface {
    private static final List<FurnitureBlock> STOVES = new ArrayList<>();
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public StoveBlock(Properties settings) {
        super(settings);
        if (this.getClass().isAssignableFrom(StoveBlock.class)){
            STOVES.add(new FurnitureBlock(this, "stove"));
        }
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(OPEN, false));
    }
    public static Stream<FurnitureBlock> streamStoves() {
        return STOVES.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, OPEN);
    }

    @Override
    protected void openContainer(Level world, BlockPos pos, Player player) {
        openMenuScreen(world, pos, player);
    }

    @ExpectPlatform
    public static void openMenuScreen(Level world, BlockPos pos, Player player) {

    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return onUseCookingForBlockheads(state, world, pos, player, hand, hit);
        } else {
            if (world.isClientSide) {
                return ItemInteractionResult.SUCCESS;
            }
            if (hit.getDirection() == Direction.UP && world.getBlockEntity(pos) instanceof StoveBlockEntity) {
                ItemStack itemStack;
                StoveBlockEntity stoveBlockEntity;
                Optional<RecipeHolder<CampfireCookingRecipe>> optional;
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof StoveBlockEntity && (optional = (stoveBlockEntity = (StoveBlockEntity)blockEntity).getRecipeFor(itemStack = player.getItemInHand(hand))).isPresent()) {
                    if (stoveBlockEntity.addItem(player.getAbilities().instabuild ? itemStack.copy() : itemStack, optional.get().value().getCookingTime())) {
                        player.awardStat(Statistics.STOVE_OPENED);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
                if(blockEntity instanceof StoveBlockEntity){
                    stoveBlockEntity = (StoveBlockEntity)blockEntity;
                    for (int i = 0; i < stoveBlockEntity.getItemsBeingCooked().size(); i++) {
                        ItemStack currentItemStack = stoveBlockEntity.getItemsBeingCooked().get(i);
                        if (currentItemStack.isEmpty()) continue;
                        if(world.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(currentItemStack), world).isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, stoveBlockEntity.removeItemNoUpdate(i));
                            world.addFreshEntity(itemEntity);
                            player.awardStat(Statistics.STOVE_OPENED);
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                    return ItemInteractionResult.CONSUME;
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            else{
                this.openContainer(world, pos, player);
            }
            return ItemInteractionResult.CONSUME;
        }
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    protected static final VoxelShape STOVE = Shapes.or(box(0, 0, 1, 16, 1, 16),box(0, 1, 0, 16, 16, 16),box(0, 16, 15, 16, 19, 16));
    protected static final VoxelShape STOVE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, STOVE);
    protected static final VoxelShape STOVE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, STOVE);
    protected static final VoxelShape STOVE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, STOVE);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir) {
            case WEST -> STOVE_WEST;
            case NORTH -> STOVE;
            case SOUTH -> STOVE_SOUTH;
            default -> STOVE_EAST;
        };
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }
        double x = (double)pos.getX() + 0.5;
        double y = pos.getY();
        double z = (double)pos.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            world.playLocalSound(x, y, z, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        int min = 0;
        int max = 3;
        int griddleChosen = (int)Math.floor(Math.random()*(max-min+1)+min);
        switch (griddleChosen) {
            case 0 -> world.addParticle(ParticleTypes.SMOKE, x - 0.25, y + 1.1, z - 0.2, 0.0, 0.0, 0.0);
            case 1 -> world.addParticle(ParticleTypes.SMOKE, x + 0.25, y + 1.1, z - 0.2, 0.0, 0.0, 0.0);
            case 2 -> world.addParticle(ParticleTypes.SMOKE, x + 0.25, y + 1.1, z + 0.2, 0.0, 0.0, 0.0);
            case 3 -> world.addParticle(ParticleTypes.SMOKE, x - 0.25, y + 1.1, z + 0.2, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return getModdedTicker(world, state, type);
    }

    @ExpectPlatform
    public static <T extends BlockEntity> BlockEntityTicker<T> getModdedTicker(Level world, BlockState state, BlockEntityType<T> type){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemInteractionResult onUseCookingForBlockheads(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult){
        throw new AssertionError();
    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StoveBlockEntity)
        {
            if (world instanceof ServerLevel) {
                Containers.dropContents(world, pos, (StoveBlockEntity)blockEntity);
                ((StoveBlockEntity)blockEntity).getRecipesToAwardAndPopExperience((ServerLevel) world, Vec3.atCenterOf(pos));
                Containers.dropContents(world, pos, ((StoveBlockEntity)blockEntity).getItemsBeingCooked());
            }
            world.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public RenderType getCustomRenderLayer() {
        return RenderType.translucent();
    }
}
