package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
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

public class ModernChair extends BasicChair {
    public float height = 0.36f;

    private static final List<FurnitureBlock> WOOD_MODERN_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_MODERN_CHAIRS = new ArrayList<>();
    public ModernChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ModernChair.class)){
            WOOD_MODERN_CHAIRS.add(new FurnitureBlock(this, "chair_modern"));
        }
        else if (this.getClass().isAssignableFrom(ModernChair.class)){
            STONE_MODERN_CHAIRS.add(new FurnitureBlock(this, "chair_modern"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodModernChairs() {
        return WOOD_MODERN_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneModernChairs() {
        return STONE_MODERN_CHAIRS.stream();
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(1, 0, 2.62 ,3, 18, 4.62), createCuboidShape(1, 9, 11.36, 3, 18, 13.36), createCuboidShape(11, 0, 2.62, 13, 9, 4.62), createCuboidShape(11, 0, 11.36, 13, 9, 13.36), createCuboidShape(0.75, 14.4, 1.59, 3.05, 23.4, 14.59 ) ,  createCuboidShape(1.98, 8, 1.6, 14.28, 10.5, 14.6));
    protected static final VoxelShape FACE_SOUTH = VoxelShapes.union(createCuboidShape(2.62, 0, 13,4.62, 18, 15), createCuboidShape(11.36, 9, 13, 13.36, 18, 15), createCuboidShape(11.36, 0, 3, 13.36, 9, 5), createCuboidShape(2.62, 0, 3, 4.62, 9, 5), createCuboidShape(1.59, 14.4, 12.95, 14.59, 23.4, 15.25 ), createCuboidShape(1.6, 8, 1.72, 14.6, 10.5, 14.02));
    protected static final VoxelShape FACE_NORTH = VoxelShapes.union(createCuboidShape(11.38, 0, 1,13.38, 18, 3), createCuboidShape(2.64, 0, 1, 4.64, 18, 3), createCuboidShape(2.64, 0, 11, 4.64, 9, 13), createCuboidShape(11.38, 0, 11, 13.38, 9, 13), createCuboidShape(1.41, 14.4, 0.75, 14.41, 23.4, 3.05), createCuboidShape(1.4, 8, 1.98, 14.4, 10.5, 14.28));
    protected static final VoxelShape FACE_EAST = VoxelShapes.union(createCuboidShape(13, 0, 11.38,15, 18, 13.38),createCuboidShape(13, 0, 2.64,15, 18, 4.64), createCuboidShape(3, 0, 11.38, 5, 9, 13.38), createCuboidShape(3, 0, 2.64, 5, 9, 4.64), createCuboidShape(12.95, 14.4, 1.41, 15.25, 23.4, 14.41), createCuboidShape(1.72, 8, 1.4, 14.02, 10.5, 14.4));
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

