package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.StairShape;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

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
    protected DyeColor getColor (BlockState state) {
        return state.get(this.COLORID);
    }
    public void dropKit(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DyeColor dyeColor = getColor(state);
        System.out.println("Dye Color in break: " + dyeColor);
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



