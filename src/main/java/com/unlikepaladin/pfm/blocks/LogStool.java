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

public class LogStool extends BasicChair {
    private static final List<FurnitureBlock> WOOD_LOG_STOOLS = new ArrayList<>();
    public LogStool(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false).with(TUCKED, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(LogStool.class)){
            WOOD_LOG_STOOLS.add(new FurnitureBlock(this, "_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodLogStools() {
        return WOOD_LOG_STOOLS.stream();
    }

    protected static final VoxelShape COLLISION = VoxelShapes.union(createCuboidShape(3, 0, 3, 13, 11, 13));

    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
                return COLLISION;
        }

}

