package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.SinkBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.BasicToiletBlock.checkType;

public class KitchenSinkBlock extends AbstractSinkBlock {
    private static final List<FurnitureBlock> WOOD_SINKS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_SINKS = new ArrayList<>();

    public KitchenSinkBlock(Settings settings, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronBehavior> map) {
        super(settings, precipitationPredicate, map);
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenSinkBlock.class)){
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

    private static final VoxelShape FACING_NORTH = VoxelShapes.combineAndSimplify(VoxelShapes.union(VoxelShapes.fullCube(), createCuboidShape(1.0625, 11.3, 0.296,15.0625, 16.3, 12.296)),VoxelShapes.union(createCuboidShape(2, 11, 2.3,14, 16.3, 11.3),createCuboidShape(0, 0, 13,16, 14, 16),createCuboidShape(0, 0, 12,16, 1, 13)), BooleanBiFunction.ONLY_FIRST);
    private static final VoxelShape FACING_EAST = VoxelShapes.combineAndSimplify(VoxelShapes.union(VoxelShapes.fullCube(), createCuboidShape(3.704, 11.3, 1.0625,15.704, 16.3, 15.0625)), VoxelShapes.union(createCuboidShape(4.7, 11, 2,13.7, 16.3, 14),createCuboidShape(0,0,0,3, 14, 16),createCuboidShape(3, 0, 0,4, 1, 16)), BooleanBiFunction.ONLY_FIRST);
    private static final VoxelShape FACING_SOUTH = VoxelShapes.combineAndSimplify(VoxelShapes.union(VoxelShapes.fullCube(), createCuboidShape(0.9375, 11.3, 3.704,14.9375, 16.3, 15.704)), VoxelShapes.union(createCuboidShape(2, 11, 4.7,14, 16.3, 13.7),createCuboidShape(0, 0, 0,16, 14, 3),createCuboidShape(0, 0, 3,16, 1, 4)), BooleanBiFunction.ONLY_FIRST);
    private static final VoxelShape FACING_WEST = VoxelShapes.combineAndSimplify(VoxelShapes.union(VoxelShapes.fullCube(), createCuboidShape(0.296, 11.3, 0.9375,12.296, 16.3, 14.9375)), VoxelShapes.union(createCuboidShape(2.3, 11, 2,11.3, 16.3, 14),createCuboidShape(13, 0, 0,16, 14, 16),createCuboidShape(12, 0, 0,13, 1, 16)), BooleanBiFunction.ONLY_FIRST);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        switch (dir) {
            case NORTH: return FACING_NORTH;
            case SOUTH: return FACING_SOUTH;
            case EAST: return FACING_EAST;
            default: return FACING_WEST;
        }
    }

    public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }
}
