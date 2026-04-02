package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.SimpleStoolBlock.rotateShape;

public class MirrorBlock extends HorizontalDirectionalBlock {

    protected static List<FurnitureBlock> mirrorBlockList = new ArrayList<>();
    public static final MapCodec<MirrorBlock> CODEC = createCodec(MirrorBlock::new);

    public MirrorBlock(BlockBehaviour.Properties settings) {
        super(settings);
        mirrorBlockList.add(new FurnitureBlock(this, "mirror"));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamMirrorBlocks() {
        return mirrorBlockList.stream();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction.getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING) && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    protected static final VoxelShape MIRROR_NORTH = Shapes.or(box(0, 0, 0,16, 16, 1));
    protected static final VoxelShape MIRROR_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_EAST = rotateShape(Direction.NORTH, Direction.EAST, MIRROR_NORTH);
    protected static final VoxelShape MIRROR_WEST = rotateShape(Direction.NORTH, Direction.WEST, MIRROR_NORTH);

    public boolean canConnect(BlockState neighborState, BlockState state)
    {
        return PaladinFurnitureMod.getPFMConfig().doDifferentMirrorsConnect() ? neighborState.getBlock() instanceof MirrorBlock && neighborState.getValue(FACING) == state.getValue(FACING) : neighborState.getBlock() == state.getBlock() && neighborState.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
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
}