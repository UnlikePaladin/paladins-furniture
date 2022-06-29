package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FroggyChair extends BasicChair {
    public float height = 0.36f;

    private static final List<FurnitureBlock> FROGGY_CHAIRS = new ArrayList<>();
    public FroggyChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        if (this.getClass().isAssignableFrom(FroggyChair.class)){
            FROGGY_CHAIRS.add(new FurnitureBlock(this, "froggy_chair_"));
        }
    }

    public static Stream<FurnitureBlock> streamFroggyChair() {
        return FROGGY_CHAIRS.stream();
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(0.3282, 10.46875, 1.5939 ,2.6282, 22.46875, 14.5939), createCuboidShape(0.3282, 22.46875, 9.5939, 2.6282, 24.46875, 14.5939), createCuboidShape(0.3282, 22.46875, 1.5939, 2.6282, 24.46875, 6.5939), createCuboidShape(0.32813, 8.1, 1.59375, 14.32813, 10.5, 14.59375), createCuboidShape(11, 0, 11.15025, 13.5, 8.78841, 13.65025 ) ,  createCuboidShape(1, 0, 11.06363, 3.5, 8.62429, 13.56363), createCuboidShape(1, 0, 2.486288, 3.5, 8.97338, 4.98628), createCuboidShape(11, 0, 2.48628, 13.5, 8.77338, 4.98628));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(1.5939, 10.46875, 13.3718,14.5939, 22.46875, 15.6718), createCuboidShape(9.5939, 22.46875, 13.3718, 14.5939, 24.46875, 15.6718), createCuboidShape(1.5939, 22.46875, 13.3718, 6.5939, 24.46875, 15.6718), createCuboidShape(1.59375, 8.1, 1.67187, 14.59375, 10.5, 15.67187), createCuboidShape(11.15025, 0, 2.5, 13.65025, 8.78841, 5 ), createCuboidShape(11.06363, 0, 12.5, 13.56363, 8.62429, 15 ), createCuboidShape(2.48628, 0.82662, 12.5, 4.98628, 8.97338, 15), createCuboidShape(2.48628, 0.82662, 2.5,4.98628, 8.77338, 5));
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(1.4061, 10.46875, 0.3282 ,14.4061, 22.46875, 2.6282), createCuboidShape(1.4061, 22.46875, 0.3282, 6.4061, 24.46875, 2.6282), createCuboidShape(9.4061, 22.46875, 0.3282, 14.4061, 24.46875, 2.6282), createCuboidShape(1.40625, 8.1, 0.32813, 14.40625, 10.5, 14.32813), createCuboidShape(2.34975, 0, 11, 4.84975, 8.78841, 13.5 ), createCuboidShape(2.43637, 0, 1, 4.93637, 8.62429, 3.5), createCuboidShape(11.01372, 0, 1, 13.51372, 8.97338, 3.5), createCuboidShape(11.01372, 0, 11, 13.51372, 8.77338, 13.5));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape( 13.3718, 10.46875, 1.4061 ,15.6718, 22.46875, 14.4061),createCuboidShape(13.3718, 22.46875, 1.4061,15.6718, 24.46875, 6.4061), createCuboidShape(13.3718, 22.46875, 9.4061, 15.6718, 24.46875, 14.4061), createCuboidShape(1.67187, 8.1, 1.40625, 15.67187, 10.5, 14.40625), createCuboidShape(2.5, 0, 2.34975, 5, 8.78841, 4.84975), createCuboidShape(12.5, 0, 2.43637, 15, 8.62429, 4.93637), createCuboidShape(12.5, 0, 11.01372, 15, 8.97338, 13.51372), createCuboidShape(2.5, 0, 11.01372, 5, 8.77338, 13.51372) );
    @SuppressWarnings("deprecated")
    @Override

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return FACE_NORTH;
            case SOUTH:
                return FACE_SOUTH;
            case EAST:
            default:
                return FACE_EAST;
        }
    }


}

