package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.StoveBlockEntityImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.PFMCookingForBlockHeadsCompat;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StoveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? PFMCookingForBlockHeadsCompat.getStoveBlockEntity(pos , state) : new StoveBlockEntityImpl(BlockEntities.STOVE_BLOCK_ENTITY, pos, state);
    }

    public static void openMenuScreen(World world, BlockPos pos, PlayerEntity player) {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            PFMCookingForBlockHeadsCompat.openMenuScreen(world, pos, player);
        } else {
            MenuProvider screenHandlerFactory = world.getBlockState(pos).getMenuProvider(world, pos);
            if (screenHandlerFactory != null) {
                // With this call the server will request the client to open the appropriate Screenhandler
                player.openMenu(screenHandlerFactory);
                player.awardStat(Statistics.STOVE_OPENED);
            }
        }
    }

    public static <T extends BlockEntity> BlockEntityTicker<T> getModdedTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if (PaladinFurnitureMod.getModList().contains("cookingforblockheads")) {
            return PFMCookingForBlockHeadsCompat.getStoveTicker(world, type);
        } else {
            if (world.isClientSide) {
                return createTickerHelper(type, BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntity::clientTick);
            } else {
                return createTickerHelper(type, BlockEntities.STOVE_BLOCK_ENTITY, StoveBlockEntity::litServerTick);
            }
        }
    }

    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    public static InteractionResult onUseCookingForBlockheads(BlockState blockState, World world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        return PFMCookingForBlockHeadsCompat.onUseStove(blockState, world, pos, player, hand, blockHitResult);
    }
}
