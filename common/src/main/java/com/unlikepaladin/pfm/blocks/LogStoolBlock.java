package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogStoolBlock extends BasicChairBlock {
    private static final List<FurnitureBlock> WOOD_LOG_STOOLS = new ArrayList<>();
    public LogStoolBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(TUCKED, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(LogStoolBlock.class)){
            WOOD_LOG_STOOLS.add(new FurnitureBlock(this, "_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodLogStools() {
        return WOOD_LOG_STOOLS.stream();
    }

    protected static final VoxelShape COLLISION = VoxelShapes.union(createCuboidShape(3, 0, 3, 13, 11, 13));

    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, COLLISION);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, COLLISION);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, COLLISION);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, COLLISION);

    @Override
    public boolean canTuck(BlockState state) {
        return (super.canTuck(state) || state.getBlock() instanceof KitchenCounterBlock);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        if (state.get(TUCKED)) {
            return switch (dir) {
                case WEST -> FACE_WEST_TUCKED;
                case NORTH -> FACE_NORTH_TUCKED;
                case SOUTH -> FACE_SOUTH_TUCKED;
                default -> FACE_EAST_TUCKED;
            };
        }
        return COLLISION;
    }
}

