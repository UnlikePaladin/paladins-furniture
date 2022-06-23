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

public class ModernStool extends BasicChair {
    public float height;

    private static final List<ModernStool> WOOD_MODERN_STOOLS = new ArrayList<>();
    private static final List<ModernStool> STONE_MODERN_STOOLS = new ArrayList<>();
    public ModernStool(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        this.height = 1.0f;
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(ModernStool.class)){
            WOOD_MODERN_STOOLS.add(this);
        }
        else if (this.getClass().isAssignableFrom(ModernStool.class)){
            STONE_MODERN_STOOLS.add(this);
        }
    }

    public static Stream<ModernStool> streamWoodModernStools() {
        return WOOD_MODERN_STOOLS.stream();
    }
    public static Stream<ModernStool> streamStoneModernStools() {
        return STONE_MODERN_STOOLS.stream();
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
    protected static final VoxelShape MODERN_STOOL_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MODERN_STOOL);
    protected static final VoxelShape MODERN_STOOL_EAST = rotateShape(Direction.NORTH, Direction.EAST, MODERN_STOOL);
    protected static final VoxelShape MODERN_STOOL_WEST = rotateShape(Direction.NORTH, Direction.WEST, MODERN_STOOL);
    @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> MODERN_STOOL_EAST;
            case NORTH -> MODERN_STOOL_SOUTH;
            case SOUTH -> MODERN_STOOL;
            default -> MODERN_STOOL_WEST;
        };
    }


}

