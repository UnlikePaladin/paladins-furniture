package com.unlikepaladin.pfm.blocks;

import com.mojang.serialization.MapCodec;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HerringbonePlankBlock extends HorizontalDirectionalBlock {
    private static final List<FurnitureBlock> PLANKS = new ArrayList<>();
    public static final MapCodec<HerringbonePlankBlock> CODEC = createCodec(HerringbonePlankBlock::new);

    public HerringbonePlankBlock(Properties settings) {
        super(settings);
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(HerringbonePlankBlock.class)){
            PLANKS.add(new FurnitureBlock(this, "herringbone_planks"));
        }
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    public static Stream<FurnitureBlock> streamPlanks() {
        return PLANKS.stream();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }
}
