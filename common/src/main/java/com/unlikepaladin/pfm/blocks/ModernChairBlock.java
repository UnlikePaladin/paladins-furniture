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

public class ModernChairBlock extends BasicChairBlock {
    public float height = 0.36f;
    private static final List<FurnitureBlock> WOOD_MODERN_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_MODERN_CHAIRS = new ArrayList<>();
    public ModernChairBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        if(AbstractSittableBlock.isWoodBased(this.getDefaultState()) && this.getClass().isAssignableFrom(ModernChairBlock.class)){
            WOOD_MODERN_CHAIRS.add(new FurnitureBlock(this, "chair_modern"));
        }
        else if (this.getClass().isAssignableFrom(ModernChairBlock.class)){
            STONE_MODERN_CHAIRS.add(new FurnitureBlock(this, "chair_modern"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodModernChairs() {
        return WOOD_MODERN_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneModernChairs() {
        return STONE_MODERN_CHAIRS.stream();
    }

    protected static final VoxelShape FACE_WEST = Shapes.or(box(0.8, 14.4, 1.6,3, 23.4, 14.6), box(2, 8, 1.6, 14.3, 10.5, 14.6), box(1, 0, 2.6, 3, 14.5, 4.6), box(11, 0, 11.4, 13, 9, 13.4), box(1, 0, 11.4, 3, 14.5, 13.4),  box(11, 0, 2.6, 13, 9, 4.6));
    protected static final VoxelShape FACE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
    protected static final VoxelShape FACE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
    protected static final VoxelShape FACE_EAST = rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);
    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, FACE_NORTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, FACE_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, FACE_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, FACE_WEST);
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

