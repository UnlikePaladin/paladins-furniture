package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.ClassicChair.rotateShape;

public class Microwave extends HorizontalFacingBlockWEntity{
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty POWERED = Properties.POWERED;

    private final Block baseBlock;
    private final BlockState baseBlockState;

    public Microwave(Settings settings) {
        super(settings);
        setDefaultState(this.getStateManager().getDefaultState().with(OPEN, false).with(POWERED, false).with(FACING, Direction.NORTH));
        this.baseBlockState = this.getDefaultState();
        this.baseBlock = baseBlockState.getBlock();
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
        stateManager.add(OPEN);
        stateManager.add(POWERED);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                //player.incrementStat(StatisticsRegistry.FREEZER_OPENED);
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
                PiglinBrain.onGuardedBlockInteracted(player, true);
            }
        }
        return ActionResult.SUCCESS;
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof MicrowaveBlockEntity) {
            ((MicrowaveBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, PaladinFurnitureMod.MICROWAVE_BLOCK_ENTITY);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends MicrowaveBlockEntity> expectedType) {
        return world.isClient ? null : Microwave.checkType(givenType, expectedType, MicrowaveBlockEntity::tick);
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    private final VoxelShape MICROWAVE = VoxelShapes.union(createCuboidShape(1.8, 0, 3.8,14.2, 1, 13.2),createCuboidShape(2, 1, 4,14, 7, 13));
    private final VoxelShape MICROWAVE_OPEN = VoxelShapes.union(createCuboidShape(1.8, 0, 3.8,14.2, 1, 13.2),createCuboidShape(2, 1, 4,14, 7, 13),createCuboidShape(2, 1, 13,3.1, 7, 20.9));

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case NORTH ->{
                if (state.get(OPEN)) {
                    return MICROWAVE_OPEN;
                 }
                return MICROWAVE;
            }
            case SOUTH -> {
                if (state.get(OPEN)) {
                    return rotateShape(Direction.NORTH, Direction.SOUTH, MICROWAVE_OPEN);
                }
                return rotateShape(Direction.NORTH, Direction.SOUTH, MICROWAVE);
            }
            case EAST ->{
                if (state.get(OPEN)) {
                    return rotateShape(Direction.NORTH, Direction.EAST, MICROWAVE_OPEN);
                }
                return rotateShape(Direction.NORTH, Direction.EAST, MICROWAVE);
            }
            case WEST -> {
                if (state.get(OPEN)) {
                    return rotateShape(Direction.NORTH, Direction.WEST, MICROWAVE_OPEN);
                }
                return rotateShape(Direction.NORTH, Direction.WEST, MICROWAVE);
            }
        }
        return MICROWAVE;
    }
}
