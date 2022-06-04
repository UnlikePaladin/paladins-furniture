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

public class ModernStool extends BasicChair {
    public float height;


    public ModernStool(Settings settings) {
        super(settings);
    setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 1.0f;
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

    protected static final VoxelShape MODERN_STOOL = VoxelShapes.union(createCuboidShape(7.125, 1, 7 ,9.125, 10, 9), createCuboidShape(5.125, 0, 5, 11.125, 1, 11), createCuboidShape(4.625, 10, 4.5, 11.625, 12, 11.5), createCuboidShape(4.625, 12, 9.5, 11.625, 15, 11.5));
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case WEST:
                return rotateShape(Direction.NORTH, Direction.EAST, MODERN_STOOL);
            case NORTH:
                return rotateShape(Direction.NORTH, Direction.SOUTH, MODERN_STOOL);
            case SOUTH:
                return MODERN_STOOL;
            case EAST:
            default:
                return rotateShape(Direction.NORTH, Direction.WEST, MODERN_STOOL);


        }
    }


}

