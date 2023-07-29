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

public class DinnerChairBlock extends BasicChairBlock {
    public float height = 0.36f;

    private static final List<FurnitureBlock> WOOD_DINNER_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_DINNER_CHAIRS = new ArrayList<>();
    public DinnerChairBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(TUCKED, false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(DinnerChairBlock.class)){
            WOOD_DINNER_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_dinner"));
        }
        else if (this.getClass().isAssignableFrom(DinnerChairBlock.class)){
            STONE_DINNER_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_dinner"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodDinnerChairs() {
        return WOOD_DINNER_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneDinnerChairs() {
        return STONE_DINNER_CHAIRS.stream();
    }

    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(2.6, 12.4, 3.5,3.9, 21.4, 12.5), createCuboidShape(3.99, 8.8, 1.6, 14.99, 10.3, 14.4), createCuboidShape(4, 8, 1.5, 15.1, 9, 14.5), createCuboidShape(2, 0, 1.5, 4, 22, 3.5), createCuboidShape(12, 0, 12.5, 14, 8, 14.5),  createCuboidShape(2, 0, 12.5, 4, 22, 14.5), createCuboidShape(12, 0, 1.5, 14, 8, 3.5));
    protected static final VoxelShape FACE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
    protected static final VoxelShape FACE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
    protected static final VoxelShape FACE_EAST = rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);
    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, FACE_NORTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, FACE_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, FACE_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, FACE_WEST);
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
        return switch (dir) {
            case WEST -> FACE_WEST;
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            default -> FACE_EAST;
        };
    }

}

