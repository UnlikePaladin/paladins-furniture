package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

    public static final MapCodec<PFMCookingTableBlock> CODEC = simpleCodec(PFMCookingTableBlock::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
            if (player.isShiftKeyDown()) {
                ItemStack noFilterBook = cookingTable.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.getInventory().add(noFilterBook)) {
                        player.drop(noFilterBook, false);
                    }

                    cookingTable.setNoFilterBook(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }

            if (!world.isClientSide()) {
                Balm.getNetworking().openMenu(player, cookingTable);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!itemStack.isEmpty()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
                if (!cookingTable.hasNoFilterBook() && itemStack.getItem() == ModItems.noFilterBook) {
                    cookingTable.setNoFilterBook(itemStack.split(1));
                    return InteractionResult.SUCCESS;
                }
            }

        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean isMoving) {
        CookingTableBlockEntity tileEntity = (CookingTableBlockEntity) level.getBlockEntity(pos);
        ItemUtils.spawnItemStack(level, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, tileEntity.getNoFilterBook());
        super.affectNeighborsAfterRemoval(state, level, pos, isMoving);
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
