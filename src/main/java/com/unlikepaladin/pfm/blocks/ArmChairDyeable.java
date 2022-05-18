package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static com.unlikepaladin.pfm.blocks.KitchenDrawer.rotateShape;

public class ArmChairDyeable extends ArmChair implements DyeableFurniture {
    public static final EnumProperty<ArmChairShape> SHAPE = EnumProperty.of("shape", ArmChairShape.class);

    public ArmChairDyeable(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(COLORID, DyeColor.WHITE).with(DYED, false).with(SHAPE, ArmChairShape.STRAIGHT));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }

    private BlockState baseBlockState;
    private Block baseBlock;
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(COLORID);
        stateManager.add(DYED);
        stateManager.add(SHAPE);


    }



    protected static final VoxelShape STANDARD = VoxelShapes.union(createCuboidShape(12, 0, 12 ,14.5, 3, 14.5),createCuboidShape(12, 0, 1.5,14.5, 3, 4), createCuboidShape(1, 0, 1.5, 3.5, 3, 4), createCuboidShape(1, 0, 12, 3.5, 3, 14.5), createCuboidShape(6.6, 2, 13, 16.3, 13.71, 16), createCuboidShape(6.6, 2, 0, 16.3, 13.71, 3), createCuboidShape(0.3, 2, 3, 16.3, 10.51, 13), createCuboidShape(0.3, 10.5, 3, 5.3, 25.51, 13), createCuboidShape(0.3, 2, 13, 6.6, 25.51, 16), createCuboidShape(0.3, 2, 0, 6.6, 25.51, 3));
    protected static final VoxelShape MIDDLE = VoxelShapes.union(createCuboidShape(0, 2, 0.3,16, 9.51, 16),createCuboidShape(0, 9.5, 0.3,16, 25.51, 5.3), createCuboidShape(0, 9.5, 5.3, 16, 10.5, 16));
    protected static final VoxelShape OUTER = VoxelShapes.union(createCuboidShape(0, 2, 0,16, 10.51, 15.7),createCuboidShape(0, 10.5, 10.7,5.3, 25.51, 15.7), createCuboidShape(0.3, 2, 15.7, 5.3, 25.51, 16),createCuboidShape(5.3, 2, 15.7,16, 10.51, 16),createCuboidShape(12.5, 0, 1.7,15, 3, 4.2),createCuboidShape(1, 0, 11.7,3.5, 3, 14.2));
    protected static final VoxelShape LEFT_EDGE = VoxelShapes.union(createCuboidShape(1.5, 0, 12,4, 3, 14.5),createCuboidShape(1.5, 0, 1,4, 3, 3.5), createCuboidShape(0, 2, 6.6, 3, 13.71, 16),createCuboidShape(3, 2, 0.3,16, 10.51, 16),createCuboidShape(3, 10.5, 0.3,16, 25.51, 5.3),createCuboidShape(0, 2, 0.3,3, 25.51, 6.6));
    protected static final VoxelShape RIGHT_EDGE = VoxelShapes.union(createCuboidShape(12.5, 0, 12,15, 3, 14.5),createCuboidShape(12.5, 0, 1,15, 3, 3.5), createCuboidShape(13, 2, 6.6, 16, 13.71, 16),createCuboidShape(0, 2, 0.3,13, 10.51, 16),createCuboidShape(0, 10.5, 0.3,13, 25.51, 5.3),createCuboidShape(13, 2, 0.3,16, 25.51, 6.6));
    protected static final VoxelShape INNER = VoxelShapes.union(createCuboidShape(12.5, 0, 12,15, 3, 14.5),createCuboidShape(1, 0, 1.5,3.5, 3, 4), createCuboidShape(0.3, 2, 0.3, 16, 10.51, 16),createCuboidShape(0.3, 10.5, 5.3,5.3, 25.51, 16),createCuboidShape(0.3, 10.5, 0.3,16, 25.51, 5.3));

    @SuppressWarnings("deprecated")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(FACING);
        ArmChairShape shape = state.get(SHAPE);
        switch(shape) {
            case STRAIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, STANDARD);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, STANDARD);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, STANDARD);
                else
                    return STANDARD;
            case MIDDLE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.NORTH, MIDDLE);
                if (dir.equals(Direction.NORTH))
                    return MIDDLE;
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.EAST, MIDDLE);
                else
                    return rotateShape(Direction.WEST, Direction.SOUTH, MIDDLE);
            case OUTER_LEFT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, OUTER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, OUTER);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
                else
                    return OUTER;
            case OUTER_RIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.SOUTH, OUTER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.EAST, OUTER);
                if (dir.equals(Direction.SOUTH))
                    return OUTER;
                else
                    return rotateShape(Direction.WEST, Direction.NORTH, OUTER);
            case LEFT_EDGE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.NORTH, LEFT_EDGE);
                if (dir.equals(Direction.NORTH))
                    return LEFT_EDGE;
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.EAST, LEFT_EDGE);
                else
                    return rotateShape(Direction.WEST, Direction.SOUTH, LEFT_EDGE);
            case RIGHT_EDGE:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.NORTH, RIGHT_EDGE);
                if (dir.equals(Direction.NORTH))
                    return RIGHT_EDGE;
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.EAST, RIGHT_EDGE);
                else
                    return rotateShape(Direction.WEST, Direction.SOUTH, RIGHT_EDGE);
            case INNER_RIGHT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.EAST, INNER);
                if (dir.equals(Direction.NORTH))
                    return rotateShape(Direction.WEST, Direction.NORTH, INNER);
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.SOUTH, INNER);
                else
                    return INNER;
            case INNER_LEFT:
                if (dir.equals(Direction.EAST))
                    return rotateShape(Direction.WEST, Direction.NORTH, INNER);
                if (dir.equals(Direction.NORTH))
                    return INNER;
                if (dir.equals(Direction.SOUTH))
                    return rotateShape(Direction.WEST, Direction.EAST, INNER);
                else
                    return rotateShape(Direction.WEST, Direction.SOUTH, INNER);
            default:
                return STANDARD;


        }
    }



    protected DyeColor getColor (BlockState state) {
        return state.get(this.COLORID);
    }
    public void dropKit(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DyeColor dyeColor = getColor(state);
        if (!player.getAbilities().creativeMode && !world.isClient && state.get(DYED)){
            switch (dyeColor){
                case RED:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_RED));     break;

                case ORANGE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_ORANGE));    break;

                case YELLOW:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_YELLOW));    break;

                case LIME:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIME));    break;

                case GREEN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_GREEN));    break;

                case CYAN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_CYAN));    break;

                case LIGHT_BLUE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_BLUE));    break;

                case BLUE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BLUE));    break;

                case PURPLE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_PURPLE));    break;

                case MAGENTA:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_MAGENTA));    break;

                case PINK:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_PINK));    break;

                case BROWN:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BROWN));    break;

                case BLACK:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_BLACK));    break;

                case GRAY:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_GRAY));    break;

                case LIGHT_GRAY:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_LIGHT_GRAY)); break;

                case WHITE:
                    dropStack(world, pos, new ItemStack(BlockItemRegistry.DYE_KIT_WHITE));    break;

                default:
                    dropStack(world, pos, new ItemStack(Items.AIR));

            }
        }
    }


    public static boolean isArmChair(BlockState state) {
        return state.getBlock() instanceof ArmChairDyeable;
    }


    private static ArmChairShape getShape(BlockState state, BlockView world, BlockPos pos) {
        Direction direction3;
        Object direction2;
        Direction direction = state.get(FACING);
        BlockState blockState = world.getBlockState(pos.offset(direction));
        boolean right = canConnect(world, pos, state.get(FACING).rotateYCounterclockwise(), state.get(FACING));
        boolean left = canConnect(world, pos, state.get(FACING).rotateYClockwise(), state.get(FACING));
        if (ArmChairDyeable.isArmChair(blockState) && ((Direction)(direction2 = blockState.get(FACING))).getAxis() != state.get(FACING).getAxis() && ArmChairDyeable.isDifferentOrientation(state, world, pos, ((Direction)direction2).getOpposite())) {
            if (direction2 == direction.rotateYCounterclockwise()) {
                return ArmChairShape.OUTER_LEFT;
            }
            return ArmChairShape.OUTER_RIGHT;
        }
        direction2 = world.getBlockState(pos.offset(direction.getOpposite()));
        if (ArmChairDyeable.isArmChair((BlockState)direction2) && (direction3 = (Direction) ((State)direction2).get(FACING)).getAxis() != state.get(FACING).getAxis() && ArmChairDyeable.isDifferentOrientation(state, world, pos, direction3)) {
            if (direction3 == direction.rotateYCounterclockwise()) {
                return ArmChairShape.INNER_LEFT;
            }
            return ArmChairShape.INNER_RIGHT;
        }
        if (left && right) {
            return ArmChairShape.MIDDLE;
        }
        else if (left) {
            return ArmChairShape.LEFT_EDGE;
        }
        else if (right) {
            return ArmChairShape.RIGHT_EDGE;
        }
        return ArmChairShape.STRAIGHT;
    }

    private static boolean canConnect(BlockView world, BlockPos pos, Direction direction, Direction tableDirection)
    {
        BlockState state = world.getBlockState(pos.offset(direction));
        boolean canConnect = (state.getBlock() instanceof ArmChairDyeable);

        return canConnect;
    }

    private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos.offset(dir));
        return !ArmChairDyeable.isArmChair(blockState) || blockState.get(FACING) != state.get(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        return direction.getAxis().isHorizontal() ? state.with(FACING, state.get(FACING)).with(SHAPE, getShape(state, world, pos)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        dropKit(world, pos, state, player);
        super.onBreak(world, pos, state, player);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        return baseBlockState.with(SHAPE, getShape(blockState, ctx.getWorld(), ctx.getBlockPos())).with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(state.getBlock())) {
            this.baseBlockState.neighborUpdate(world, pos, Blocks.AIR, pos, false);
            this.baseBlock.onBlockAdded(this.baseBlockState, world, pos, oldState, false);
        }
    }


}

enum ArmChairShape implements StringIdentifiable
{
    STRAIGHT("straight"),
    INNER_LEFT("inner_left"),
    INNER_RIGHT("inner_right"),
    OUTER_LEFT("outer_left"),
    OUTER_RIGHT("outer_right"),
    MIDDLE("middle"),
    LEFT_EDGE("left_edge"),
    RIGHT_EDGE("right_edge");


    private final String name;

    ArmChairShape(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}



