package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.ToiletBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.DinnerTable.rotateShape;

public class BasicToilet extends BasicChair implements BlockEntityProvider {
    private static final List<FurnitureBlock> BASIC_TOILET = new ArrayList<>();
    public static final EnumProperty<ToiletState> TOILET_STATE = EnumProperty.of("toilet", ToiletState.class);
    public BasicToilet(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false).with(TOILET_STATE, ToiletState.CLEAN));
        if(this.getClass().isAssignableFrom(BasicChair.class)){
            BASIC_TOILET.add(new FurnitureBlock(this, "basic_toilet"));
        }
    }
    public static Stream<FurnitureBlock> streamBasicToilet() {
        return BASIC_TOILET.stream();
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(TOILET_STATE);
        super.appendProperties(stateManager);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isSneaking()) {
            world.setBlockState(pos, state.with(TOILET_STATE, ToiletState.FLUSHING));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        //TODO: Particle effects with potion, brown
        if(state.get(TOILET_STATE) != ToiletState.DIRTY) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextDouble() < 0.2)
            world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y + 0.6, z, 0.8, 1.0, 0.0);
        if (random.nextDouble() < 0.009) {
            world.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y + 0.6, z, 0.18, 0.0, 0.34);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToiletBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntityRegistry.TOILET_BLOCK_ENTITY, ToiletBlockEntity::tick);
    }

    public static void setClean(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, state.with(BasicToilet.TOILET_STATE, ToiletState.CLEAN), NOTIFY_ALL);
    }
    protected static final VoxelShape TOILET_WEST = VoxelShapes.union(createCuboidShape(2, 1, 4.2,14, 6, 11.7),createCuboidShape(1, 0, 3.2,15, 1, 12.7),createCuboidShape(5, 5, 2.5,16, 10, 13.5),createCuboidShape(0, 6, 2.5,5, 20, 13.5),createCuboidShape(5, 10, 3.5,6, 21, 12.5));
    protected static final VoxelShape TOILET_NORTH = rotateShape(Direction.WEST, Direction.NORTH, TOILET_WEST);
    protected static final VoxelShape TOILET_EAST = rotateShape(Direction.WEST, Direction.EAST, TOILET_WEST);
    protected static final VoxelShape TOILET_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, TOILET_WEST);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir){
            case NORTH -> TOILET_NORTH;
            case SOUTH -> TOILET_SOUTH;
            case EAST -> TOILET_EAST;
            default -> TOILET_WEST;
        };
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

}

