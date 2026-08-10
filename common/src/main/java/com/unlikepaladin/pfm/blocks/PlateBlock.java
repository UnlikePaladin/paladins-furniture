package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.Statistics;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class PlateBlock extends HorizontalFacingBlockWithEntity {

    public static final BooleanProperty CUTLERY = BooleanProperty.create("cutlery");

    private static final List<FurnitureBlock> PLATES = new ArrayList<>();
    public PlateBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CUTLERY, false));
        PLATES.add(new FurnitureBlock(this, "plate"));
    }

    public static Stream<FurnitureBlock> streamPlates() {
        return PLATES.stream();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        PlateBlockEntity plateBlockEntity;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity && (itemStack.isEdible())) {
            if (!world.isClientSide && ((PlateBlockEntity)blockEntity).addItem(player.getAbilities().instabuild ? itemStack.copy() : itemStack)) {
                player.awardStat(Statistics.PLATE_USED);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        if(BuiltInRegistries.BLOCK.get(BuiltInRegistries.ITEM.getKey(itemStack.getItem())) instanceof CutleryBlock) {
            world.setBlockAndUpdate(pos, state.setValue(CUTLERY, true));
            itemStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        if (player.isShiftKeyDown() && blockEntity instanceof PlateBlockEntity) {
            plateBlockEntity = (PlateBlockEntity)blockEntity;
            if (!plateBlockEntity.getItemInPlate().isEmpty()) {
                if (!world.isClientSide) {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, plateBlockEntity.removeItem());
                    world.addFreshEntity(itemEntity);
                    player.awardStat(Statistics.PLATE_USED);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        if(blockEntity instanceof PlateBlockEntity){
            plateBlockEntity = (PlateBlockEntity)blockEntity;
                if (!plateBlockEntity.getItemInPlate().isEmpty()) {
                    ItemStack stack = plateBlockEntity.getItemInPlate();
                    spawnItemParticles(player, stack, 16);
                    if (BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals("sandwichable:sandwich")) {
                       eatSandwich(stack, world, player);
                    }
                    else {
                        if (!player.isCreative()) {
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, stack.finishUsingItem(world, player));
                            world.addFreshEntity(itemEntity);
                        }
                        player.eat(world, stack);
                    }
                    plateBlockEntity.removeItem();
                    player.awardStat(Statistics.PLATE_USED);
                    return InteractionResult.SUCCESS;
                }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @ExpectPlatform
    public static void eatSandwich(ItemStack stack, Level world, Player player) {

    }
    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(CUTLERY) && !player.getAbilities().instabuild) {
            ItemEntity itemEntity = new ItemEntity( world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(PaladinFurnitureModBlocksItems.BASIC_CUTLERY, 1));
            world.addFreshEntity(itemEntity);
        }
        super.playerWillDestroy(world, pos, state, player);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(CUTLERY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(world, pos)) {
            if (state.getValue(CUTLERY)) {
                ItemEntity itemEntity = new ItemEntity((Level) world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(PaladinFurnitureModBlocksItems.BASIC_CUTLERY, 1));
                world.addFreshEntity(itemEntity);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    protected static final VoxelShape PLATE = Shapes.or(box(2,0,3, 12,1,13));
    protected static final VoxelShape PLATE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, PLATE);
    protected static final VoxelShape PLATE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, PLATE);
    protected static final VoxelShape PLATE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, PLATE);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir) {
            case WEST -> PLATE_SOUTH;
            case NORTH -> PLATE_WEST;
            case SOUTH -> PLATE_EAST;
            default -> PLATE;
        };
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.is(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
            Containers.dropContents(world, pos, plateBlockEntity.getContainer());
            world.updateNeighbourForOutputSignal(pos, this);
            plateBlockEntity.setRemoved();
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    protected final Random random = new Random();
    private void spawnItemParticles(LivingEntity entity, ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3 vec3d = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.xRot(-entity.getXRot() * ((float)Math.PI / 180));
            vec3d = vec3d.yRot(-entity.getYRot() * ((float)Math.PI / 180));
            double d = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
            Vec3 vec3d2 = new Vec3(((double)this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
            vec3d2 = vec3d2.xRot(-entity.getXRot() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.yRot(-entity.getYRot() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.add(entity.getX(), entity.getEyeY(), entity.getZ());
            entity.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
        }
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = Direction.DOWN;
        return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
    }
}
