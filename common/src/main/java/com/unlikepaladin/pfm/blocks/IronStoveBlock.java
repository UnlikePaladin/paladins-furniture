package com.unlikepaladin.pfm.blocks;


import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class IronStoveBlock extends StoveBlock {
    private static final List<FurnitureBlock> IRON_STOVES = new ArrayList<>();
    public IronStoveBlock(Properties settings) {
        super(settings);
        if (this.getClass().isAssignableFrom(IronStoveBlock.class)){
            IRON_STOVES.add(new FurnitureBlock(this, "stove"));
        }
    }

    public static Stream<FurnitureBlock> streamIronStoves() {
        return IRON_STOVES.stream();
    }

    protected static final VoxelShape IRON_STOVE = Shapes.or(box(0, 0, 1, 16, 1, 16),box(0, 1, 0, 16, 16, 16));
    protected static final VoxelShape IRON_STOVE_SOUTH = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, IRON_STOVE);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        switch (dir) {
            case WEST: {
                return IRON_STOVE_WEST;
            }
            case NORTH: {
                return IRON_STOVE;
            }
            case SOUTH: {
                return IRON_STOVE_SOUTH;
            }
            default: {
                return IRON_STOVE_EAST;
            }
        }
    }
}
