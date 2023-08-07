package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class MirrorBlock extends HorizontalFacingBlock {

    protected static List<FurnitureBlock> mirrorBlockList = new ArrayList<>();
    public MirrorBlock(Settings settings) {
        super(settings);
        mirrorBlockList.add(new FurnitureBlock(this, "mirror"));
    }

    public static Stream<FurnitureBlock> streamMirrorBlocks() {
        return mirrorBlockList.stream();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING) && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    protected static final VoxelShape MIRROR_NORTH = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 1));
    protected static final VoxelShape MIRROR_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIRROR_NORTH);

    public boolean canConnect(BlockState neighborState, BlockState state)
    {
        return PaladinFurnitureMod.getPFMConfig().doDifferentMirrorsConnect() ? neighborState.getBlock() instanceof MirrorBlock && neighborState.get(FACING) == state.get(FACING) : neighborState.getBlock() == state.getBlock() && neighborState.get(FACING) == state.get(FACING);
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
}