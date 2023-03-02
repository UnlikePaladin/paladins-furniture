package com.unlikepaladin.pfm.compat.fabric.sandwichable.blocks;

import com.unlikepaladin.pfm.compat.fabric.sandwichable.PFMSandwichableRegistry;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.blocks.blockentities.PFMToasterBlockEntity;
import io.github.foundationgames.sandwichable.blocks.BlocksRegistry;
import io.github.foundationgames.sandwichable.blocks.ToasterBlock;
import io.github.foundationgames.sandwichable.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.unlikepaladin.pfm.blocks.LogTableBlock.rotateShape;

public class PFMToasterBlock extends ToasterBlock {
    public PFMToasterBlock(Settings settings) {
        super(settings);
    }
    public static final VoxelShape IRON_TOASTER = VoxelShapes.union(createCuboidShape(4.8, 0, 3.8,11.2, 1, 12.2),createCuboidShape(5, 1, 4,11, 6, 12));
    public static final VoxelShape IRON_TOASTER_WEST_EAST = rotateShape(Direction.NORTH, Direction.WEST, IRON_TOASTER);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        return switch (dir) {
            case EAST, WEST -> IRON_TOASTER_WEST_EAST;
            default -> IRON_TOASTER;
        };
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PFMToasterBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, PFMSandwichableRegistry.IRON_TOASTER_BLOCKENTITY, PFMToasterBlockEntity::tick);
    }


    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof PFMToasterBlockEntity ? ((PFMToasterBlockEntity)world.getBlockEntity(pos)).getComparatorOutput() : 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        super.neighborUpdate(state, world, pos, block, neighborPos, moved);
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            boolean toasting = ((PFMToasterBlockEntity)world.getBlockEntity(pos)).isToasting();
            world.setBlockState(pos, world.getBlockState(pos).with(ON, toasting));
        }

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            boolean toasting = ((PFMToasterBlockEntity)world.getBlockEntity(pos)).isToasting();
            world.setBlockState(pos, world.getBlockState(pos).with(ON, toasting));
        }

    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PFMToasterBlockEntity) {
                PFMToasterBlockEntity blockEntity = (PFMToasterBlockEntity)world.getBlockEntity(pos);

                for(int i = 0; i < 2; ++i) {
                    ItemEntity item = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, blockEntity.getItems().get(i));
                    world.spawnEntity(item);
                }

                world.updateNeighbors(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof PFMToasterBlockEntity) {
            PFMToasterBlockEntity blockEntity = (PFMToasterBlockEntity) world.getBlockEntity(pos);
            if (!player.isSneaking()) {
                if (!blockEntity.isToasting()) {
                    ItemEntity itemEntity;
                    if (!player.getStackInHand(hand).isEmpty() && !player.getStackInHand(hand).getItem().equals(BlocksRegistry.SANDWICH.asItem())) {
                        if (!blockEntity.addItem(hand, player)) {
                            itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.8D, (double)pos.getZ() + 0.5D, blockEntity.takeItem(player));
                            world.spawnEntity(itemEntity);
                        }
                    } else {
                        itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.8D, (double)pos.getZ() + 0.5D, blockEntity.takeItem(player));
                        world.spawnEntity(itemEntity);
                    }
                }

                Util.sync(blockEntity, world);
            } else if (!blockEntity.isToasting()) {
                blockEntity.startToasting(player);
            } else {
                blockEntity.stopToasting(player);
            }
        }

        return ActionResult.success(world.isClient());
    }

}
