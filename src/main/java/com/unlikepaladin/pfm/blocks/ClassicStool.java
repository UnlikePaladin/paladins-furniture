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

public class ClassicStool extends BasicChair {
    public float height;

    private static final List<FurnitureBlock> WOOD_CLASSIC_STOOLS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_CLASSIC_STOOLS = new ArrayList<>();
    public ClassicStool(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false).with(TUCKED, false));
        this.height = 0.5f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ClassicStool.class)){
            WOOD_CLASSIC_STOOLS.add(new FurnitureBlock(this, "classic_stool"));
        }
        else if (this.getClass().isAssignableFrom(ClassicStool.class)){
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

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
    protected static final VoxelShape CLASSIC_STOOL = VoxelShapes.union(createCuboidShape(3.625, 0, 3.5,5.625, 10, 5.5), createCuboidShape(10.625, 0, 3.5, 12.625, 10, 5.5), createCuboidShape(10.625, 0, 10.5, 12.625, 20, 12.5), createCuboidShape(3.625, 10, 3.5, 12.625, 12, 10.5), createCuboidShape(5.625, 10, 10.5, 10.625, 12, 12.5), createCuboidShape(5.625, 15, 11, 10.625, 19.5, 12),createCuboidShape(3.625, 0, 10.5,5.625, 20, 12.5));
    protected static final VoxelShape CLASSIC_STOOL_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, CLASSIC_STOOL);
    protected static final VoxelShape CLASSIC_STOOL_WEST = rotateShape(Direction.NORTH, Direction.WEST, CLASSIC_STOOL);
    protected static final VoxelShape CLASSIC_STOOL_EAST = rotateShape(Direction.NORTH, Direction.EAST, CLASSIC_STOOL);
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> CLASSIC_STOOL_EAST;
            case NORTH -> CLASSIC_STOOL_SOUTH;
            case SOUTH -> CLASSIC_STOOL;
            default -> CLASSIC_STOOL_WEST;
        };
    }


}

