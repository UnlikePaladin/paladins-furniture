package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PFMCookingTableBlock extends BaseEntityBlock {
    protected PFMCookingTableBlock(Properties arg) {
        super(arg);
    }

    MapCodec<PFMCookingTableBlock> CODEC = simpleCodec(PFMCookingTableBlock::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldItem, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        CookingTableBlockEntity blockEntity = (CookingTableBlockEntity)world.getBlockEntity(pos);
        if (!heldItem.isEmpty()) {
            if (blockEntity != null) {
                if (!blockEntity.hasNoFilterBook() && heldItem.getItem() == ModItems.noFilterBook) {
                    blockEntity.setNoFilterBook(heldItem.split(1));
                    return ItemInteractionResult.SUCCESS;
                }
            }
        } else if (player.isShiftKeyDown() && blockEntity != null) {
            ItemStack noFilterBook = blockEntity.getNoFilterBook();
            if (!noFilterBook.isEmpty()) {
                if (!player.getInventory().add(noFilterBook)) {
                    player.drop(noFilterBook, false);
                }
                blockEntity.setNoFilterBook(ItemStack.EMPTY);
                return ItemInteractionResult.SUCCESS;
            }
        }
        if (!world.isClientSide) {
            Balm.getNetworking().openGui(player, blockEntity);
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        CookingTableBlockEntity tileEntity = (CookingTableBlockEntity) level.getBlockEntity(pos);
        if (tileEntity != null && !state.is(newState.getBlock())) {
            ItemUtils.spawnItemStack(level, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, tileEntity.getNoFilterBook());
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CookingTableBlockEntity(pos, state);
    }

    private static final VoxelShape SHAPE = Shapes.or(box(3, 0, 3, 13,1,13));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
