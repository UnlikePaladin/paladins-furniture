package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
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

public class SimpleStoolBlock extends BasicChairBlock {
    public float height;
    private static final List<FurnitureBlock> WOOD_SIMPLE_STOOLS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_SIMPLE_STOOLS = new ArrayList<>();
    public SimpleStoolBlock(Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(TUCKED, false));
        this.height = 0.5f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(SimpleStoolBlock.class)){
            WOOD_SIMPLE_STOOLS.add(new FurnitureBlock(this, "simple_stool"));
        }
        else if (this.getClass().isAssignableFrom(SimpleStoolBlock.class)){
            STONE_SIMPLE_STOOLS.add(new FurnitureBlock(this, "simple_stool"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodSimpleStools() {
        return WOOD_SIMPLE_STOOLS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneSimpleStools() {
        return STONE_SIMPLE_STOOLS.stream();
    }

    @Override
    public boolean canTuck(BlockState state) {
        return (super.canTuck(state) || state.getBlock() instanceof KitchenCounterBlock);
    }

    protected static VoxelShape SIMPLE_STOOL = Shapes.or(box(3.5, 0, 3.5,5.5, 10, 5.5),box(10.5, 0, 3.5,12.5, 10, 5.5),box(10.5, 0, 10.5,12.5, 10, 12.5),box(3.5, 10, 3.5,12.5, 12, 12.5),box(3.5, 0, 10.5,5.5, 10, 12.5));
    protected static final VoxelShape FACE_NORTH_TUCKED = PFMShapeUtil.tuckShape(Direction.NORTH, SIMPLE_STOOL);
    protected static final VoxelShape FACE_SOUTH_TUCKED = PFMShapeUtil.tuckShape(Direction.SOUTH, SIMPLE_STOOL);
    protected static final VoxelShape FACE_EAST_TUCKED = PFMShapeUtil.tuckShape(Direction.EAST, SIMPLE_STOOL);
    protected static final VoxelShape FACE_WEST_TUCKED = PFMShapeUtil.tuckShape(Direction.WEST, SIMPLE_STOOL);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        if (state.getValue(TUCKED)) {
            switch (dir) {
                case WEST: {
                    return FACE_WEST_TUCKED;
                }
                case NORTH: {
                    return FACE_NORTH_TUCKED;
                }
                case SOUTH: {
                    return FACE_SOUTH_TUCKED;
                }
                default: {
                    return FACE_EAST_TUCKED;
                }
            }
        }
        return SIMPLE_STOOL;
    }



}

