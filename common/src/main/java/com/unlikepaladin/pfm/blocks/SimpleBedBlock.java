package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.PFMBedBlockEntity;
import com.unlikepaladin.pfm.client.PFMBuiltinItemRendererExtension;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.LogTableBlock.rotateShape;

public class SimpleBedBlock extends BedBlock implements DyeableFurnitureBlock, PFMBuiltinItemRendererExtension {
    private static final List<FurnitureBlock> SIMPLE_BEDS = new ArrayList<>();
    private final DyeColor color;
    public SimpleBedBlock(DyeColor color, Properties settings) {
        super(color, settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
        if(this.getClass().isAssignableFrom(SimpleBedBlock.class)){
            String bedColor = color.getName();
            SIMPLE_BEDS.add(new FurnitureBlock(this, bedColor+"_simple_bed"));
        }
        this.color = color;
    }

    public static Stream<FurnitureBlock> streamSimpleBeds() {
        return SIMPLE_BEDS.stream();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
        Direction direction = ctx.getHorizontalDirection();
        BlockPos blockPos = ctx.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(direction);
        if (ctx.getLevel().getBlockState(blockPos2).canBeReplaced(ctx)) {
            return blockState;
        }
        return null;
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.CONSUME;
        }
        if (state.getValue(PART) != BedPart.HEAD && !((state = world.getBlockState(pos = pos.relative(state.getValue(FACING)))).getBlock() instanceof SimpleBedBlock)) {
            return InteractionResult.CONSUME;
        }
        if (!BedBlock.canSetSpawn(world)) {
            world.removeBlock(pos, false);
            BlockPos blockPos = pos.relative(state.getValue(FACING).getOpposite());
            if (world.getBlockState(blockPos).is(this)) {
                world.removeBlock(blockPos, false);
            }
            world.explode(null, world.getDamageSources().badRespawnPointExplosion(blockPos.getCenter()), null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, 5.0f, true, Level.ExplosionInteraction.BLOCK);
            return InteractionResult.SUCCESS;
        }
        if (state.getValue(OCCUPIED)) {
            if (!this.isFree(world, pos)) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            }
            return InteractionResult.SUCCESS;
        }
        player.startSleepInBed(pos).ifLeft(reason -> {
            if (reason.getMessage() != null) {
                player.displayClientMessage(reason.getMessage(), true);
            }
        });
        return InteractionResult.SUCCESS;
    }

    private boolean isFree(Level world, BlockPos pos) {
        List<Villager> list = world.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        }
        list.get(0).stopSleeping();
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherPart(state.getValue(PART), state.getValue(FACING))) {
            if (neighborState.getBlock() instanceof SimpleBedBlock && neighborState.getValue(PART) != state.getValue(PART)) {
                return state.setValue(OCCUPIED, neighborState.getValue(OCCUPIED));
            }
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockPos blockPos;
        BlockState blockState;
        BedPart bedPart;
        if (!world.isClientSide && player.isCreative() && (bedPart = state.getValue(PART)) == BedPart.FOOT && (blockState = world.getBlockState(blockPos = pos.relative(getDirectionTowardsOtherPart(bedPart, state.getValue(FACING))))).is(this) && blockState.getValue(PART) == BedPart.HEAD) {
            world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
            world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
        }
        this.spawnDestroyParticles(world, player, pos, state);
        if (state.is(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(player, false);
        }
        world.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    static final VoxelShape HEAD = Shapes.or(box(0, 9, 0,16, 14, 3),box(0, 0, 0,16, 9, 16));
    static final VoxelShape HEAD_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, HEAD);
    static final VoxelShape HEAD_EAST = rotateShape(Direction.NORTH, Direction.EAST, HEAD);
    static final VoxelShape HEAD_WEST = rotateShape(Direction.NORTH, Direction.WEST, HEAD);

    static final VoxelShape FOOT_EAST = Shapes.or(box(0, 9, 0,3, 10, 16),box(0, 0, 0,16, 9, 16));
    static final VoxelShape FOOT_SOUTH = rotateShape(Direction.EAST, Direction.SOUTH, FOOT_EAST);
    static final VoxelShape FOOT_WEST = rotateShape(Direction.EAST, Direction.WEST, FOOT_EAST);
    static final VoxelShape FOOT_NORTH = rotateShape(Direction.EAST, Direction.NORTH, FOOT_EAST);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getBedShape(state.getValue(FACING), state.getValue(PART), HEAD, FOOT_NORTH, HEAD_EAST, FOOT_EAST, HEAD_WEST, FOOT_WEST, HEAD_SOUTH, FOOT_SOUTH);
    }

    static VoxelShape getBedShape(Direction direction, BedPart bedPart2, VoxelShape head, VoxelShape footNorth, VoxelShape headEast, VoxelShape footEast, VoxelShape headWest, VoxelShape footWest, VoxelShape headSouth, VoxelShape footSouth) {
        switch (direction){
            case NORTH -> {
                if(bedPart2 == BedPart.HEAD){
                    return head;
                }
                return footNorth;
            }
            case EAST -> {
                if(bedPart2 == BedPart.HEAD){
                    return headEast;
                }
                return footEast;
            }
            case WEST -> {
                if(bedPart2 == BedPart.HEAD){
                    return headWest;
                }
                return footWest;
            }
            default -> {
                if(bedPart2 == BedPart.HEAD){
                    return headSouth;
                }
                return footSouth;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public DyeColor getPFMColor() {
        return color;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PFMBedBlockEntity(pos, state, this.color);
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
