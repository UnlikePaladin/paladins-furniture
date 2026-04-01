package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassicStoolBlock extends BasicChairBlock {
    public float height;

    private static final List<FurnitureBlock> WOOD_CLASSIC_STOOLS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_STOOLS = new ArrayList<>();
    public ClassicStoolBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        this.height = 0.5f;
        if(isWoodBased(this.getDefaultState()) && this.getClass().isAssignableFrom(ClassicStoolBlock.class)){
            WOOD_CLASSIC_STOOLS.add(new FurnitureBlock(this, "classic_stool"));
        }
        else if (this.getClass().isAssignableFrom(ClassicStoolBlock.class)){
            STONE_CLASSIC_STOOLS.add(new FurnitureBlock(this, "classic_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicStools() {
        return WOOD_CLASSIC_STOOLS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicStools() {
        return STONE_CLASSIC_STOOLS.stream();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx);
    }

    protected static final VoxelShape CLASSIC_STOOL_SOUTH = Shapes.or(box(3.625, 0, 3.5,5.625, 10, 5.5), box(10.625, 0, 3.5, 12.625, 10, 5.5), box(10.625, 0, 10.5, 12.625, 20, 12.5), box(3.625, 10, 3.5, 12.625, 12, 10.5), box(5.625, 10, 10.5, 10.625, 12, 12.5), box(5.625, 15, 11, 10.625, 19.5, 12),box(3.625, 0, 10.5,5.625, 20, 12.5));
    protected static final VoxelShape CLASSIC_STOOL = rotateShape(Direction.NORTH, Direction.SOUTH, CLASSIC_STOOL_SOUTH);
    protected static final VoxelShape CLASSIC_STOOL_EAST = rotateShape(Direction.NORTH, Direction.WEST, CLASSIC_STOOL_SOUTH);
    protected static final VoxelShape CLASSIC_STOOL_WEST = rotateShape(Direction.NORTH, Direction.EAST, CLASSIC_STOOL_SOUTH);
    protected static final VoxelShape FACE_SOUTH_TUCKED = tuckShape(Direction.SOUTH, CLASSIC_STOOL_SOUTH);
    protected static final VoxelShape FACE_NORTH_TUCKED = tuckShape(Direction.NORTH, CLASSIC_STOOL);
    protected static final VoxelShape FACE_WEST_TUCKED = tuckShape(Direction.WEST, CLASSIC_STOOL_WEST);
    protected static final VoxelShape FACE_EAST_TUCKED = tuckShape(Direction.EAST, CLASSIC_STOOL_EAST);

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
            case WEST -> CLASSIC_STOOL_WEST;
            case NORTH -> CLASSIC_STOOL;
            case SOUTH -> CLASSIC_STOOL_SOUTH;
            default -> CLASSIC_STOOL_EAST;
        };
    }


}

