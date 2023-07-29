package com.unlikepaladin.pfm.blocks;


import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronStoveBlock extends StoveBlock {
    private static final List<FurnitureBlock> IRON_STOVES = new ArrayList<>();
    public IronStoveBlock(Settings settings) {
        super(settings);
        if (this.getClass().isAssignableFrom(IronStoveBlock.class)){
            IRON_STOVES.add(new FurnitureBlock(this, "stove"));
        }
    }

    public static Stream<FurnitureBlock> streamIronStoves() {
        return IRON_STOVES.stream();
    }

    protected static final VoxelShape IRON_STOVE = VoxelShapes.union(createCuboidShape(0, 0, 0, 16, 16, 14),createCuboidShape(0, 1, 14, 16, 16, 15),createCuboidShape(1.8, 12.2, 15.5375, 14.3, 12.799, 16.1375),createCuboidShape(2.5, 12.2, 14.07, 3.1, 12.79, 15.56),createCuboidShape(12.6, 12.2, 14.07, 13.2, 12.79, 15.57),createCuboidShape(1.8, 2.89, 15.437, 14.3, 3.49, 16.037),createCuboidShape(2.5, 2.89, 13.47, 3.1, 3.49, 15.47),createCuboidShape(12.6, 2.89, 13.47, 13.2, 3.49, 15.47));
    protected static final VoxelShape IRON_STOVE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_EAST = rotateShape(Direction.NORTH, Direction.EAST, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_WEST = rotateShape(Direction.NORTH, Direction.WEST, IRON_STOVE);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case WEST -> IRON_STOVE_EAST;
            case NORTH -> IRON_STOVE_SOUTH;
            case SOUTH -> IRON_STOVE;
            default -> IRON_STOVE_WEST;
        };
    }
}
