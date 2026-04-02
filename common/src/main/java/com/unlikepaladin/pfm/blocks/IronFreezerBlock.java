package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class IronFreezerBlock extends FreezerBlock {
    private Supplier<FridgeBlock> fridge;
    public IronFreezerBlock(Properties settings, Supplier<FridgeBlock> fridge) {
        super(settings, fridge);
        this.fridge = fridge;
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos);
    }

    protected static final Map<Direction, VoxelShape> FREEZER = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(3, 13, -0.5,13, 14, 1),box(1, 1, 1,15, 16, 2),box(1, 0, 2,15, 32, 16)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(3, 13, -0.5,13, 14, 1),box(1, 1, 1,15, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(3, 13, -3.5,13, 14, -2),box(1, 0, 2,15, 32, 16),box(1, 1, -2,15, 16, 3)));}};
    protected static final Map<Direction, VoxelShape> FREEZER_SINGLE_OPEN = new HashMap<>() {{put(Direction.NORTH, Shapes.or(box(3, 13, -3.5,13, 14, -2),box(1, 1, -2,15, 16, 3),box(1, 0, 3,15, 16, 16)));}};

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING).getOpposite();
        Boolean open = state.getValue(OPEN);
        boolean hasFridge = world.getBlockState(pos.above()).getBlock() instanceof IronFridgeBlock;
        if (hasFridge) {
            if (open) {
                if (!FREEZER_OPEN.containsKey(dir))
                    FREEZER_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_OPEN.get(Direction.NORTH)));
                return FREEZER_OPEN.get(dir);
            }
            if (!FREEZER.containsKey(dir))
                FREEZER.put(dir, rotateShape(Direction.NORTH, dir, FREEZER.get(Direction.NORTH)));
            return FREEZER.get(dir);
        } else {
            if (open) {
                if (!FREEZER_SINGLE_OPEN.containsKey(dir))
                    FREEZER_SINGLE_OPEN.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_SINGLE_OPEN.get(Direction.NORTH)));
                return FREEZER_SINGLE_OPEN.get(dir);
            }
            if (!FREEZER_SINGLE.containsKey(dir))
                FREEZER_SINGLE.put(dir, rotateShape(Direction.NORTH, dir, FREEZER_SINGLE.get(Direction.NORTH)));
            return FREEZER_SINGLE.get(dir);
        }
    }
    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
    }
    protected void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
    }
}
