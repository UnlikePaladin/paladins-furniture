package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.data.FurnitureBlock;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SmokerBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.unlikepaladin.pfm.blocks.KitchenDrawerBlock.rotateShape;

public class KitchenCounterOvenBlock extends SmokerBlock implements DynamicRenderLayerInterface {
    private static final List<FurnitureBlock> WOOD_COUNTER_OVENS = new ArrayList<>();
    private static final List<FurnitureBlock> STONE_COUNTER_OVENS = new ArrayList<>();

    public KitchenCounterOvenBlock(Properties settings) {
        super(settings.lightLevel((state) -> 0).emissiveRendering((blockstate, b, c) -> false));
        if((material.equals(Material.WOOD) || material.equals(Material.NETHER_WOOD)) && this.getClass().isAssignableFrom(KitchenCounterOvenBlock.class)){
            WOOD_COUNTER_OVENS.add(new FurnitureBlock(this, "kitchen_counter_oven"));
        }
        else if (this.getClass().isAssignableFrom(KitchenCounterOvenBlock.class)){
            STONE_COUNTER_OVENS.add(new FurnitureBlock(this, "kitchen_counter_oven"));
        }
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(OPEN, false));
    }

    public static Stream<FurnitureBlock> streamWoodCounterOvens() {
        return WOOD_COUNTER_OVENS.stream();
    }
    public static Stream<FurnitureBlock> streamStoneCounterOvens() {
        return STONE_COUNTER_OVENS.stream();
    }

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return OvenBlockEntity.getFactory().create(pos, state);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
        super.createBlockStateDefinition(builder);
    }
    public static boolean connectsVertical(Block block) {
        return block instanceof KitchenCounterBlock || block instanceof KitchenCounterOvenBlock;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.WOOL) {
            return 20;
        }
        return 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide() ? null : createTickerHelper(type, BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, OvenBlockEntity::serverTick);
    }

    @Override
    public void openContainer(Level world, BlockPos pos, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof OvenBlockEntity) {
            OvenBlockEntity.openScreen(player, world.getBlockState(pos), world, pos);
            player.awardStat(Statistics.STOVE_OPENED);
        }
    }

    protected static final VoxelShape COUNTER_OVEN = Shapes.or(box(0, 1, 0, 16, 14, 14),box(0, 0, 0, 16, 1, 12),box(0, 14, 0, 16, 16, 16),box(2, 10, 14.1, 14, 11, 15.1),box(4, 2, 14.1, 12, 3, 15.1));
    protected static final VoxelShape COUNTER_OVEN_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, COUNTER_OVEN);
    protected static final VoxelShape COUNTER_OVEN_EAST = rotateShape(Direction.NORTH, Direction.EAST, COUNTER_OVEN);
    protected static final VoxelShape COUNTER_OVEN_WEST = rotateShape(Direction.NORTH, Direction.WEST, COUNTER_OVEN);
    protected static final VoxelShape COUNTER_OVEN_MIDDLE = Shapes.or(box(0, 0, 0,16, 16, 13),box(0, 1, 13,16, 15, 14),box(2, 10, 14.1,14, 11, 15.1),box(4, 2, 14.1,12, 3, 15.1));
    protected static final VoxelShape COUNTER_OVEN_MIDDLE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, COUNTER_OVEN_MIDDLE);
    protected static final VoxelShape COUNTER_OVEN_MIDDLE_EAST = rotateShape(Direction.NORTH, Direction.EAST, COUNTER_OVEN_MIDDLE);
    protected static final VoxelShape COUNTER_OVEN_MIDDLE_WEST = rotateShape(Direction.NORTH, Direction.WEST, COUNTER_OVEN_MIDDLE);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(FACING);
        boolean up = connectsVertical(view.getBlockState(pos.above()).getBlock());
        boolean down = connectsVertical(view.getBlockState(pos.below()).getBlock());
        if(up || down) {
            return switch (dir){
                case NORTH -> COUNTER_OVEN_MIDDLE_SOUTH;
                case SOUTH -> COUNTER_OVEN_MIDDLE;
                case EAST -> COUNTER_OVEN_MIDDLE_WEST;
                default -> COUNTER_OVEN_MIDDLE_EAST;
            };
        }
        else {
        return switch (dir) {
            case WEST -> COUNTER_OVEN_EAST;
            case NORTH -> COUNTER_OVEN_SOUTH;
            case SOUTH -> COUNTER_OVEN;
            default -> COUNTER_OVEN_WEST;
            };
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public RenderType getCustomRenderLayer() {
        return RenderType.translucent();
    }
}
