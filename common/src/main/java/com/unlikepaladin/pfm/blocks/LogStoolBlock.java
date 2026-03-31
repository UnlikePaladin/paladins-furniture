package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogStoolBlock extends BasicChairBlock {
    private static final List<FurnitureBlock> WOOD_LOG_STOOLS = new ArrayList<>();
    public LogStoolBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(LogStoolBlock.class)){
            WOOD_LOG_STOOLS.add(new FurnitureBlock(this, "_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodLogStools() {
        return WOOD_LOG_STOOLS.stream();
    }

    protected static final VoxelShape COLLISION = Shapes.or(box(3, 0, 3, 13, 11, 13));

    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, COLLISION);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, COLLISION);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, COLLISION);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, COLLISION);

    @Override
    public boolean canTuck(BlockState state) {
        return (super.canTuck(state) || state.getBlock() instanceof KitchenCounterBlock);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(TUCKED)) {
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

