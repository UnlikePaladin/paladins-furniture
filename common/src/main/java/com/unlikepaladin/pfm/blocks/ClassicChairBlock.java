package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.block.state.BlockState;

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

public class ClassicChairBlock extends BasicChairBlock {
    public float height;
    private static final List<FurnitureBlock> WOOD_CLASSIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_CHAIRS = new ArrayList<>();
    public ClassicChairBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        this.height = 0.36f;
        if(isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ClassicChairBlock.class)){
            WOOD_CLASSIC_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicChairBlock.class)){
            STONE_CLASSIC_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_classic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicChairs() {
        return WOOD_CLASSIC_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicChairs() {
        return STONE_CLASSIC_CHAIRS.stream();
    }

    protected static final VoxelShape FACE_WEST = Shapes.or(box(2, 9, 2.05,4, 24, 13.95), box(2, 7.85, 4.05, 4, 9, 11.9), box(3.85, 8.75, 1.99, 14.95, 10.25, 13.96), box(3.8, 7.86, 1.97, 15, 9.26, 13.97), box(2, 0, 11.9, 4, 9, 13.9), box(12, 0, 11.9, 14, 9, 13.9),box(2, 0, 2.05,4, 9, 4.05),box(12, 0, 2.05,14, 9, 4.05));
    protected static final VoxelShape FACE_EAST = PFMShapeUtil.rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);
    protected static final VoxelShape FACE_NORTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
    protected static final VoxelShape FACE_SOUTH = PFMShapeUtil.rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
    protected static final VoxelShape FACE_NORTH_TUCKED = PFMShapeUtil.tuckShape(Direction.NORTH, FACE_NORTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = PFMShapeUtil.tuckShape(Direction.SOUTH, FACE_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = PFMShapeUtil.tuckShape(Direction.EAST, FACE_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = PFMShapeUtil.tuckShape(Direction.WEST, FACE_WEST);
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
        return switch (dir) {
            case WEST -> FACE_WEST;
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            default -> FACE_EAST;
        };
    }


}

