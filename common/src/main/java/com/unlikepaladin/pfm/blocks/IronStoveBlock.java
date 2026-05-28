package com.unlikepaladin.pfm.blocks;


import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronStoveBlock extends StoveBlock {
    private static final List<FurnitureBlock> IRON_STOVES = new ArrayList<>();
    public IronStoveBlock(Properties settings) {
        super(settings);
        if (this.getClass().isAssignableFrom(IronStoveBlock.class)){
            IRON_STOVES.add(new FurnitureBlock(this, "stove"));
        }
    }

    public static Stream<FurnitureBlock> streamIronStoves() {
        return IRON_STOVES.stream();
    }

    protected static final VoxelShape IRON_STOVE = Shapes.or(box(0, 0, 1, 16, 1, 16),box(0, 1, 0, 16, 16, 16));
    protected static final VoxelShape IRON_STOVE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_EAST = rotateShape(Direction.NORTH, Direction.EAST, IRON_STOVE);
    protected static final VoxelShape IRON_STOVE_WEST = rotateShape(Direction.NORTH, Direction.WEST, IRON_STOVE);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        return switch (dir) {
            case WEST -> IRON_STOVE_WEST;
            case NORTH -> IRON_STOVE;
            case SOUTH -> IRON_STOVE_SOUTH;
            default -> IRON_STOVE_EAST;
        };
    }
}
