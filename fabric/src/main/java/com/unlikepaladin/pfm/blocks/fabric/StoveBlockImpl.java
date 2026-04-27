package com.unlikepaladin.pfm.blocks.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.fabric.StoveBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class StoveBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new StoveBlockEntityImpl(BlockEntities.STOVE_BLOCK_ENTITY);
    }

    public static void openMenuScreen(Level world, BlockPos pos, Player player) {
        MenuProvider screenHandlerFactory = world.getBlockState(pos).getMenuProvider(world, pos);
        if (screenHandlerFactory != null) {
            // With this call the server will request the client to open the appropriate Screenhandler
            player.openMenu(screenHandlerFactory);
            player.awardStat(Statistics.STOVE_OPENED);
        }
    }

    public static InteractionResult onUseCookingForBlockheads(BlockState blockState, Level world, BlockPos pos, Player player, Player hand, BlockHitResult blockHitResult) {
        return InteractionResult.CONSUME;
    }
}
