package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class MirrorBlock extends HorizontalFacingBlock {
    public MirrorBlock(Settings settings) {
        super(settings);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    protected static final VoxelShape MIRROR_NORTH = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 1));
    protected static final VoxelShape MIRROR_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIRROR_NORTH);

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    public boolean canConnect(BlockState neighborState, BlockState state)
    {
        return (PaladinFurnitureMod.getPFMConfig().doDifferentMirrorsConnect() ? neighborState.getBlock() instanceof MirrorBlock : neighborState.getBlock() == state.getBlock()) && neighborState.get(FACING) == state.get(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case SOUTH: {
                return MIRROR_SOUTH;
            }
            case EAST: {
                return MIRROR_EAST;
            }
            case WEST: {
                return MIRROR_WEST;
            }
            default:
            case NORTH: {
                return MIRROR_NORTH;
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }
}