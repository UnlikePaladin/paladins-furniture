package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.utilities.PFMShapeUtil;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class IronFridgeBlock extends FridgeBlock {
    private Supplier<FreezerBlock> freezer;
    public IronFridgeBlock(Properties settings, Supplier<FreezerBlock> freezer) {
        super(settings, freezer);
        this.freezer = freezer;
    }

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(12, 2, -0.5,13, 15, 1),box(1, 1, 1,15, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(12, 0, -0.5,13, 15, 1),box(1, 0, 1,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(12, 0, -0.5,13, 16, 1),box(1, 0, 1,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(12, 2, -0.5,13, 16, 1),box(1, 1, 1,15, 16, 2),box(1, 0, 2,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(12, 1, -0.5, 13, 15, 1), box(1, 0, 1,15, 16, 2),box(1, -16, 2,15, 16, 16)));}};

    protected static final Map<Direction, VoxelShape> FRIDGE_SINGLE_OPEN = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(-0.5, 2, -10,1, 15, -9),box(1, 1, -12,3, 16, 3),box(1, 1, 3,15, 16, 16),box(1, 0, 2,15, 1, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_TOP_OPEN = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(-0.5, 0, -10,1, 15, -9),box(1, 0, -12,3, 16, 3),box(1, 0, 3,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_MIDDLE_OPEN = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(-0.5, 0, -10,1, 16, -9),box(1, 0, -12,3, 16, 3),box(1, 0, 3,15, 16, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_BOTTOM_OPEN = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(-0.5, 2, -10, 1, 16, -9),box(1, 1, -12,3, 16, 3),box(1, 1, 3,15, 16, 16),box(1, 0, 2,15, 1, 16)));}};
    protected static final Map<Direction, VoxelShape> FRIDGE_OPEN = new HashMap<>(){{put(Direction.NORTH, Shapes.or(box(1, 1, -12,3, 16, 2),box(-0.5, 1, -10,1, 15, -9),box(2, 0, 1,15, 1, 2),box(1, -16, 2,15, 16, 16)));}};


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING).getOpposite();
        Boolean open = state.getValue(OPEN);
        boolean bottom = state.is(world.getBlockState(pos.above()).getBlock());
        boolean top = state.is(world.getBlockState(pos.below()).getBlock());
        boolean hasFreezer = world.getBlockState(pos.below()).getBlock() instanceof IronFreezerBlock;

        if (top && bottom) {
            if (open) {
                if (!FRIDGE_MIDDLE_OPEN.containsKey(dir))
                    FRIDGE_MIDDLE_OPEN.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_MIDDLE_OPEN.get(dir);
            }
            if (!FRIDGE_MIDDLE.containsKey(dir))
                FRIDGE_MIDDLE.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_MIDDLE.get(Direction.NORTH)));
            return FRIDGE_MIDDLE.get(dir);
        } else if (bottom) {
            if (open) {
                if (!FRIDGE_BOTTOM_OPEN.containsKey(dir))
                    FRIDGE_BOTTOM_OPEN.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM_OPEN.get(Direction.NORTH)));
                return FRIDGE_BOTTOM_OPEN.get(dir);
            }
            if (!FRIDGE_BOTTOM.containsKey(dir))
                FRIDGE_BOTTOM.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_BOTTOM.get(Direction.NORTH)));
            return FRIDGE_BOTTOM.get(dir);
        } else if (top) {
            if (open) {
                if (!FRIDGE_TOP_OPEN.containsKey(dir))
                    FRIDGE_TOP_OPEN.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_TOP_OPEN.get(Direction.NORTH)));
                return FRIDGE_TOP_OPEN.get(dir);
            }
            if (!FRIDGE_TOP.containsKey(dir))
                FRIDGE_TOP.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_TOP.get(Direction.NORTH)));
            return FRIDGE_TOP.get(dir);
        } else if (hasFreezer) {
            if (open) {
                if (!FRIDGE_OPEN.containsKey(dir))
                    FRIDGE_OPEN.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_OPEN.get(Direction.NORTH)));
                return FRIDGE_OPEN.get(dir);
            }
            if (!FRIDGE.containsKey(dir))
                FRIDGE.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE.get(Direction.NORTH)));
            return FRIDGE.get(dir);
        } else {
            if (open) {
                if (!FRIDGE_SINGLE_OPEN.containsKey(dir))
                    FRIDGE_SINGLE_OPEN.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE_OPEN.get(Direction.NORTH)));
                return FRIDGE_SINGLE_OPEN.get(dir);
            }
            if (!FRIDGE_SINGLE.containsKey(dir))
                FRIDGE_SINGLE.put(dir, PFMShapeUtil.rotateShape(Direction.NORTH, dir, FRIDGE_SINGLE.get(Direction.NORTH)));
            return FRIDGE_SINGLE.get(dir);
        }
    }

    public void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
    }
}
