package com.unlikepaladin.pfm.blocks;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.LogTableBlock.rotateShape;

public class PFMToasterBlock extends HorizontalFacingBlockWithEntity {
    public static final BooleanProperty ON = BooleanProperty.create("on");;
    public PFMToasterBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING, ON);
    }

    public static final VoxelShape IRON_TOASTER = Shapes.or(box(5, 0, 3,11, 7, 13));
    public static final VoxelShape IRON_TOASTER_WEST_EAST = rotateShape(Direction.NORTH, Direction.WEST, IRON_TOASTER);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ctx) {
        Direction dir = state.getValue(FACING);
        switch (dir) {
            case EAST:
            case WEST:
                return IRON_TOASTER_WEST_EAST;
            default:
                return IRON_TOASTER;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockGetter world) {
        return PFMToasterBlockEntity.getFactory().get();
    }


    public int getAnalogOutputSignal(BlockState state, ServerLevel world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof PFMToasterBlockEntity ? ((PFMToasterBlockEntity)world.getBlockEntity(pos)).getAnalogOutputSignal() : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        super.neighborChanged(state, world, pos, block, neighborPos, moved);
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            boolean toasting = ((PFMToasterBlockEntity)world.getBlockEntity(pos)).isToasting();
            world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ON, toasting));
        }

    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            boolean toasting = ((PFMToasterBlockEntity)world.getBlockEntity(pos)).isToasting();
            world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(ON, toasting));
        }

    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PFMToasterBlockEntity) {
                PFMToasterBlockEntity blockEntity = (PFMToasterBlockEntity)world.getBlockEntity(pos);

                for(int i = 0; i < 2; ++i) {
                    ItemEntity item = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, blockEntity.getItems().get(i));
                    world.addFreshEntity(item);
                }

                world.updateNeighborsAt(pos, this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }

    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            PFMToasterBlockEntity blockEntity = (PFMToasterBlockEntity) world.getBlockEntity(pos);
            if (!player.isShiftKeyDown()) {
                if (!blockEntity.isToasting()) {
                    ItemEntity itemEntity;
                    if (!player.getItemInHand(hand).isEmpty() && !isSandwich(player.getItemInHand(hand))) {
                        if (!blockEntity.addItem(hand, player)) {
                            itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.8D, (double)pos.getZ() + 0.5D, blockEntity.takeItem(player));
                            world.addFreshEntity(itemEntity);
                        }
                    } else {
                        itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.8D, (double)pos.getZ() + 0.5D, blockEntity.takeItem(player));
                        world.addFreshEntity(itemEntity);
                    }
                }
                PFMToasterBlockEntity.sync(blockEntity, blockEntity.getLevel());
            } else if (!blockEntity.isToasting()) {
                blockEntity.startToasting(player);
            } else {
                blockEntity.stopToasting(player);
            }
        }

        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    @ExpectPlatform
    public static boolean isSandwich(ItemStack stack) {
        throw new AssertionError();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

}
