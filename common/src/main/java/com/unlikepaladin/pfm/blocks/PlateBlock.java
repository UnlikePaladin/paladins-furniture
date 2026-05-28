package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.PaladinFurnitureModBlocksItems;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class PlateBlock extends HorizontalFacingBlockWithEntity {

    public static final BooleanProperty CUTLERY = BooleanProperty.create("cutlery");

    private static final List<FurnitureBlock> PLATES = new ArrayList<>();
    public static final MapCodec<PlateBlock> CODEC = simpleCodec(PlateBlock::new);

    public PlateBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(CUTLERY, false));
        PLATES.add(new FurnitureBlock(this, "plate"));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamPlates() {
        return PLATES.stream();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        PlateBlockEntity plateBlockEntity;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity && (itemStack.has(DataComponents.FOOD))) {
            if (!world.isClientSide && ((PlateBlockEntity)blockEntity).addItem(player.getAbilities().instabuild ? itemStack.copy() : itemStack)) {
                player.awardStat(Statistics.PLATE_USED);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        if(BuiltInRegistries.BLOCK.getValue(BuiltInRegistries.ITEM.getKey(itemStack.getItem())) instanceof CutleryBlock) {
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
                        ItemStack result = stack.finishUsingItem(world, player);
                        if (!player.isCreative()) {
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, result);
                            world.addFreshEntity(itemEntity);
                        }
                    }
                    plateBlockEntity.removeItem();
                    player.awardStat(Statistics.PLATE_USED);
                    return InteractionResult.SUCCESS;
                }
        }
        return super.useItemOn(itemStack, state, world, pos, player, hand, hit);
    }

    @ExpectPlatform
    public static void eatSandwich(ItemStack stack, Level world, Player player) {

    }
    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(CUTLERY) && !player.getAbilities().instabuild) {
            ItemEntity itemEntity = new ItemEntity( world, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(PaladinFurnitureModBlocksItems.BASIC_CUTLERY, 1));
            world.addFreshEntity(itemEntity);
        }
        return super.playerWillDestroy(world, pos, state, player);
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
    public BlockState updateShape(BlockState state, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (!state.canSurvive(levelReader, pos)) {
            if (levelReader instanceof Level && state.getValue(CUTLERY)) {
                ItemEntity itemEntity = new ItemEntity((Level) levelReader, pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D, new ItemStack(PaladinFurnitureModBlocksItems.BASIC_CUTLERY, 1));
                ((Level) levelReader).addFreshEntity(itemEntity);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    protected static final VoxelShape PLATE = Shapes.or(box(2,0,3, 12,1,13));
    protected static final VoxelShape PLATE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, PLATE);
    protected static final VoxelShape PLATE_EAST = rotateShape(Direction.NORTH, Direction.EAST, PLATE);
    protected static final VoxelShape PLATE_WEST = rotateShape(Direction.NORTH, Direction.WEST, PLATE);
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
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
            Containers.dropContents(world, pos, plateBlockEntity.getContainer());
            world.updateNeighbourForOutputSignal(pos, this);
            plateBlockEntity.setRemoved();
        }
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
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
            entity.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
        }
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = Direction.DOWN;
        return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
    }
}
