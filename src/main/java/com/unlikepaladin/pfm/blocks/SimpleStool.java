package com.unlikepaladin.pfm.blocks;

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

public class SimpleStool extends BasicChair {
    public float height;

    private static final List<SimpleStool> WOOD_SIMPLE_STOOLS = new ArrayList<>();
    private static final List<SimpleStool> STONE_SIMPLE_STOOLS = new ArrayList<>();
    public SimpleStool(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 0.5f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(SimpleStool.class)){
            WOOD_SIMPLE_STOOLS.add(this);
        }
        else if (this.getClass().isAssignableFrom(SimpleStool.class)){
            STONE_SIMPLE_STOOLS.add(this);
        }
    }

    public static Stream<SimpleStool> streamWoodSimpleStools() {
        return WOOD_SIMPLE_STOOLS.stream();
    }
    public static Stream<SimpleStool> streamStoneSimpleStools() {
        return STONE_SIMPLE_STOOLS.stream();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(2, 9, 2.05 ,4, 24, 13.91), createCuboidShape(3.8, 7.86, 1.97, 15, 10.26, 13.97), createCuboidShape(2, 0, 11.9, 4, 9, 13.9), createCuboidShape(12, 0, 11.9, 14, 9, 13.9), createCuboidShape(2, 0, 2.05, 4, 9, 4.05), createCuboidShape(12, 0, 2.05, 14, 9, 4.05));
    @SuppressWarnings("deprecated")


    /**
     * Method to rotate VoxelShapes from this random Forge Forums thread: https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/
     */
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    protected static VoxelShape SIMPLE_STOOL = VoxelShapes.union(createCuboidShape(3.625, 0, 3.5,5.625, 10, 5.5),createCuboidShape(10.625, 0, 3.5,12.625, 10, 5.5),createCuboidShape(10.625, 0, 10.5,12.625, 10, 12.5),createCuboidShape(3.625, 10, 3.5,12.625, 12, 12.5),createCuboidShape(3.625, 0, 10.5,5.625, 10, 12.5));
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SIMPLE_STOOL;
    }


}

