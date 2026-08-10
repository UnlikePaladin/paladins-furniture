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

public class ModernStoolBlock extends BasicChairBlock {
    private static final List<FurnitureBlock> WOOD_MODERN_STOOLS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_MODERN_STOOLS = new ArrayList<>();
    public ModernStoolBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        if(isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(ModernStoolBlock.class)){
            WOOD_MODERN_STOOLS.add(new FurnitureBlock(this, "modern_stool"));
        }
        else if (this.getClass().isAssignableFrom(ModernStoolBlock.class)){
            STONE_MODERN_STOOLS.add(new FurnitureBlock(this, "modern_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodModernStools() {
        return WOOD_MODERN_STOOLS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneModernStools() {
        return STONE_MODERN_STOOLS.stream();
    }

    protected static final VoxelShape MODERN_STOOL_SOUTH = Shapes.or(box(7.125, 1, 7 ,9.125, 10, 9), box(5.125, 0, 5, 11.125, 1, 11), box(4.625, 10, 4.5, 11.625, 12, 11.5), box(4.625, 12, 9.5, 11.625, 15, 11.5));
    protected static final VoxelShape MODERN_STOOL = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.SOUTH, MODERN_STOOL_SOUTH);
    protected static final VoxelShape MODERN_STOOL_WEST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.EAST, MODERN_STOOL_SOUTH);
    protected static final VoxelShape MODERN_STOOL_EAST = PFMShapeUtil.rotateShape(Direction.NORTH, Direction.WEST, MODERN_STOOL_SOUTH);
    protected static final VoxelShape FACE_NORTH_TUCKED = PFMShapeUtil.tuckShape(Direction.NORTH, MODERN_STOOL);
    protected static final VoxelShape FACE_SOUTH_TUCKED = PFMShapeUtil.tuckShape(Direction.SOUTH, MODERN_STOOL_SOUTH);
    protected static final VoxelShape FACE_EAST_TUCKED = PFMShapeUtil.tuckShape(Direction.EAST, MODERN_STOOL_EAST);
    protected static final VoxelShape FACE_WEST_TUCKED = PFMShapeUtil.tuckShape(Direction.WEST, MODERN_STOOL_WEST);

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
        return switch (dir) {
            case WEST -> MODERN_STOOL_WEST;
            case NORTH -> MODERN_STOOL;
            case SOUTH -> MODERN_STOOL_SOUTH;
            default -> MODERN_STOOL_EAST;
        };
    }

}

