package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.Balm;
/*import net.blay09.mods.cookingforblockheads.block.entity.CookingTableBlockEntity;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.util.ItemUtils;*/
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class PFMCookingTableBlock extends BlockWithEntity {
    protected PFMCookingTableBlock(Settings arg) {
        super(arg);
    }

    public static final MapCodec<PFMCookingTableBlock> CODEC = createCodec(PFMCookingTableBlock::new);
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult blockHitResult) {
        Object blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
            if (player.isSneaking()) {
                ItemStack noFilterBook = cookingTable.getNoFilterBook();
                if (!noFilterBook.isEmpty()) {
                    if (!player.getInventory().insertStack(noFilterBook)) {
                        player.dropItem(noFilterBook, false);
                    }

                    cookingTable.setNoFilterBook(ItemStack.EMPTY);
                    return ActionResult.SUCCESS;
                }
            }

            if (!world.isClient()) {
                Balm.networking().openMenu(player, (NamedScreenHandlerFactory) cookingTable);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack itemStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!itemStack.isEmpty()) {
            Object blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CookingTableBlockEntity cookingTable) {
                if (!cookingTable.hasNoFilterBook() && itemStack.getItem() == ModItems.noFilterBook) {
                    cookingTable.setNoFilterBook(itemStack.split(1));
                    return ActionResult.SUCCESS;
                }
            }

        }
        return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    public void onStateReplaced(BlockState state, ServerWorld level, BlockPos pos, boolean isMoving) {
        CookingTableBlockEntity tileEntity = (CookingTableBlockEntity) (Object) level.getBlockEntity(pos);
        ItemUtils.spawnItemStack(level, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, tileEntity.getNoFilterBook());
        super.onStateReplaced(state, level, pos, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return (BlockEntity) (Object) new CookingTableBlockEntity(pos, state);
    }
*/
    private static final VoxelShape SHAPE = VoxelShapes.union(createCuboidShape(3, 0, 3, 13,1,13));
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
