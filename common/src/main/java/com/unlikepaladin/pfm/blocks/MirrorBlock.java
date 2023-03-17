package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class MirrorBlock extends HorizontalFacingBlock {
    public MirrorBlock(Settings settings) {
        super(settings);
    }
    private static final EnumProperty<MirrorShape> SHAPE = EnumProperty.of("shape", MirrorShape.class);
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(SHAPE);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getShape(this.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()).with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    protected static final VoxelShape MIRROR_NORTH = VoxelShapes.union(createCuboidShape(0, 0, 0,16, 16, 1));
    protected static final VoxelShape MIRROR_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIRROR_NORTH);

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    boolean canConnect(BlockState neighborState, BlockState state)
    {
        return (PaladinFurnitureMod.getPFMConfig().doDifferentMirrorsConnect() ? neighborState.getBlock() instanceof MirrorBlock : neighborState.getBlock() == state.getBlock()) && neighborState.get(FACING) == state.get(FACING);
    }

    //TODO: Investigate using Enum Sets and BitSets / flags
    private BlockState getShape(BlockState state, BlockView world, BlockPos pos) {
        int up = canConnect(world.getBlockState(pos.up()), state) ? 1 : 0;
        int down = canConnect(world.getBlockState(pos.down()), state) ? 11 : 0;
        int left = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYClockwise())), state) ? 21 : 0;
        int right = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise())), state) ? 41 : 0;
        int cornerLeftUp = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYClockwise()).up()), state) ? 3 : 0;
        int cornerRightDown = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise()).down()), state) ? 3 : 0;
        int cornerLeftDown = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYClockwise()).down()), state) ? 3 : 0;
        int cornerRightUp = canConnect(world.getBlockState(pos.offset(state.get(FACING).rotateYCounterclockwise()).up()), state) ? 3 : 0;
        int index = (up + left + right + down) - 1;
        if (index == 21)
            index += cornerLeftUp;
        if (index == 41)
            index += cornerRightUp;
        if (index == 51)
            index += cornerRightDown;
        if (index == 31)
            index += cornerLeftDown;
        if (index < 0)
            index = 39;
        return state.with(SHAPE, MirrorShape.getByIndex(index));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return getShape(state, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case SOUTH: {
                return MIRROR_SOUTH;
            }
            case EAST: {
                return MIRROR_EAST;
            }
            case WEST: {
                return MIRROR_WEST;
            }
            default:
            case NORTH: {
                return MIRROR_NORTH;
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }
}
//TODO: Use BakedModel instead
enum MirrorShape implements StringIdentifiable {
    UP(0,"up") ,
    DOWN(10,"down"),
    UP_DOWN(11,"up_down"),
    LEFT(20,"left"),
    RIGHT(40,"right"),
    LEFT_RIGHT(61,"left_right"),
    CORNER_RIGHT_TOP(51,"corner_right_top"),
    RIGHT_UP_DOWN(52,"right_up_down"),
    CORNER_LEFT_TOP(31,"corner_left_top"),
    LEFT_UP_DOWN(32,"left_up_down"),
    LEFT_TOP(34,"left_top"),
    RIGHT_TOP(54,"right_top"),
    CORNER_RIGHT_BOTTOM(41,"corner_right_bottom"),
    CORNER_LEFT_BOTTOM(21,"corner_left_bottom"),
    RIGHT_BOTTOM(44,"right_bottom"),
    LEFT_BOTTOM(24,"left_bottom"),
    MIDDLE_BOTTOM(62,"middle_bottom"),
    MIDDLE_TOP(72,"middle_top"),
    SINGLE(39,"single"),
    ALL(73,"all");

    private final String name;
    private final int index;
    MirrorShape(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static MirrorShape getByIndex(int n) {
        for (MirrorShape shape : values()) {
            if (shape.index == n) {
                return shape;
            }
        }
        PaladinFurnitureMod.GENERAL_LOGGER.warn("Wrong Index: " + n);
        return ALL;
    }
    @Override
    public String asString() {
        return name;
    }
}
