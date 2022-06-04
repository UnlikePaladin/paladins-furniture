package com.unlikepaladin.pfm.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ClassicChair extends BasicChair {
    public float height;


    public ClassicChair(Settings settings) {
        super(settings);
    setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 0.36f;
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


    @Override

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return FACE_WEST;
            case NORTH:
                return rotateShape(Direction.WEST, Direction.NORTH, FACE_WEST);
            case SOUTH:
                return rotateShape(Direction.WEST, Direction.SOUTH, FACE_WEST);
                case EAST:
            default:
                return rotateShape(Direction.WEST, Direction.EAST, FACE_WEST);


        }
    }


}

