package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.ClassicChairBlock.rotateShape;

public class MicrowaveBlock extends HorizontalFacingBlockWithEntity implements DynamicRenderLayerInterface {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private final Block baseBlock;
    private final BlockState baseBlockState;
    private static final List<FurnitureBlock> MICROWAVES = new ArrayList<>();
    public static final MapCodec<MicrowaveBlock> CODEC = simpleCodec(MicrowaveBlock::new);

    public MicrowaveBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(OPEN, false).setValue(FACING, Direction.NORTH));
        this.baseBlockState = this.defaultBlockState();
        this.baseBlock = baseBlockState.getBlock();
        MICROWAVES.add(new FurnitureBlock(this, "microwave"));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamMicrowaves() {
        return MICROWAVES.stream();
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
        stateManager.add(POWERED);
    }
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            openScreen(player, state, world, pos);
            player.awardStat(Statistics.MICROWAVE_USED);
            PiglinAi.angerNearbyPiglins((ServerWorld) world,player, true);
        }
        return InteractionResult.SUCCESS;
    }

    @ExpectPlatform
    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        return;
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(world, type, BlockEntities.MICROWAVE_BLOCK_ENTITY);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends MicrowaveBlockEntity> expectedType) {
        if (!world.isClient) {
            ServerRecipeManager.MatchGetter<SingleStackRecipeInput, SmokingRecipe> cachedcheck = ServerRecipeManager.createCachedMatchGetter(
                    RecipeType.SMOKING
            );
            return createTickerHelper(givenType, expectedType, (worldx, pos, statex, blockEntity) -> MicrowaveBlockEntity.tick(worldx, pos, statex, blockEntity, cachedcheck));
        }
        return null;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    private final VoxelShape MICROWAVE = Shapes.or(box(1, 0, 4,15, 8, 14));

    private final VoxelShape MICROWAVE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MICROWAVE);
    private final VoxelShape MICROWAVE_EAST = rotateShape(Direction.NORTH, Direction.EAST, MICROWAVE);
    private final VoxelShape MICROWAVE_WEST = rotateShape(Direction.NORTH, Direction.WEST, MICROWAVE);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case SOUTH -> {
                return MICROWAVE_SOUTH;
            }
            case EAST ->{
                return MICROWAVE_EAST;
            }
            case WEST -> {
                return MICROWAVE_WEST;
            }
            default -> {
                return MICROWAVE;
            }
        }
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
