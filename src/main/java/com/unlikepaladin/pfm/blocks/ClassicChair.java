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

public class ClassicChair extends BasicChair {
    public float height;

    private static final List<FurnitureBlock> WOOD_CLASSIC_CHAIRS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_CHAIRS = new ArrayList<>();
    public ClassicChair(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 0.36f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicChair.class)){
            WOOD_CLASSIC_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_classic"));
        }
        else if (this.getClass().isAssignableFrom(ClassicChair.class)){
            STONE_CLASSIC_CHAIRS.add(new FurnitureBlock(this.asBlock(), "chair_classic"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodClassicChairs() {
        return WOOD_CLASSIC_CHAIRS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneClassicChairs() {
        return STONE_CLASSIC_CHAIRS.stream();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
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

    protected static final VoxelShape FACE_WEST = VoxelShapes.union(createCuboidShape(2, 9, 2.05 ,4, 24, 13.91), createCuboidShape(3.8, 7.86, 1.97, 15, 10.26, 13.97), createCuboidShape(2, 0, 11.9, 4, 9, 13.9), createCuboidShape(12, 0, 11.9, 14, 9, 13.9), createCuboidShape(2, 0, 2.05, 4, 9, 4.05), createCuboidShape(12, 0, 2.05, 14, 9, 4.05));
    protected static final VoxelShape FACE_EAST = rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);
    protected static final VoxelShape FACE_NORTH = rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
    protected static final VoxelShape FACE_SOUTH = rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> FACE_WEST;
            case NORTH -> FACE_NORTH;
            case SOUTH -> FACE_SOUTH;
            default -> FACE_EAST;
        };
    }


}

