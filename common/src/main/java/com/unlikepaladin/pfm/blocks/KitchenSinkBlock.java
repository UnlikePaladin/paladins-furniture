package com.unlikepaladin.pfm.blocks;

import com.mojang.datafixers.util.Function3;
import com.unlikepaladin.pfm.blocks.blockentities.SinkBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class KitchenSinkBlock extends AbstractSinkBlock {
    private static final List<FurnitureBlock> WOOD_SINKS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_SINKS = new ArrayList<>();

    public KitchenSinkBlock(Properties settings, Biome.Precipitation precipitationPredicate, CauldronBehavior.CauldronBehaviorMap map) {
        super(settings, precipitationPredicate, map);
        if(AbstractSittableBlock.isWoodBased(this.defaultBlockState()) && this.getClass().isAssignableFrom(KitchenSinkBlock.class)){
            WOOD_SINKS.add(new FurnitureBlock(this, "kitchen_sink"));
        }
        else if (this.getClass().isAssignableFrom(KitchenSinkBlock.class)){
            STONE_SINKS.add(new FurnitureBlock(this, "kitchen_sink"));
        }
    }

    public static Stream<FurnitureBlock> streamWoodSinks() {
        return WOOD_SINKS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneSinks() {
        return STONE_SINKS.stream();
    }

    private static final VoxelShape FACING_NORTH = Shapes.join(Shapes.or(Shapes.block(), box(1.0625, 11.3, 0.296,15.0625, 16.3, 12.296)), Shapes.or(box(2, 11, 2.3,14, 16.3, 11.3),box(0, 0, 13,16, 14, 16),box(0, 0, 12,16, 1, 13)), BooleanOp.ONLY_FIRST);
    private static final VoxelShape FACING_EAST = Shapes.join(Shapes.or(Shapes.block(), box(3.704, 11.3, 1.0625,15.704, 16.3, 15.0625)), Shapes.or(box(4.7, 11, 2,13.7, 16.3, 14),box(0,0,0,3, 14, 16),box(3, 0, 0,4, 1, 16)), BooleanOp.ONLY_FIRST);
    private static final VoxelShape FACING_SOUTH = Shapes.join(Shapes.or(Shapes.block(), box(0.9375, 11.3, 3.704,14.9375, 16.3, 15.704)), Shapes.or(box(2, 11, 4.7,14, 16.3, 13.7),box(0, 0, 0,16, 14, 3),box(0, 0, 3,16, 1, 4)), BooleanOp.ONLY_FIRST);
    private static final VoxelShape FACING_WEST = Shapes.join(Shapes.or(Shapes.block(), box(0.296, 11.3, 0.9375,12.296, 16.3, 14.9375)), Shapes.or(box(2.3, 11, 2,11.3, 16.3, 14),box(13, 0, 0,16, 14, 16),box(12, 0, 0,13, 1, 16)), BooleanOp.ONLY_FIRST);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (dir) {
            case NORTH: return FACING_NORTH;
            case SOUTH: return FACING_SOUTH;
            case EAST: return FACING_EAST;
            default: return FACING_WEST;
        }
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (AbstractSittableBlock.isWoodBased(state)) {
            return 20;
        }
        return 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        //if (state.getValue(LEVEL_4) > 0)
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public Function3<Settings, Biome.Precipitation, CauldronBehavior.CauldronBehaviorMap, AbstractSinkBlock> getSinkConstructor() {
        return KitchenSinkBlock::new;
    }
}
