package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.registry.Statistics;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KitchenCounterOvenBlockImpl {
    public static void openMenuScreen(Level world, BlockPos pos, Player player) {
        MenuProvider screenHandlerFactory = world.getBlockState(pos).getMenuProvider(world, pos);
        if (screenHandlerFactory != null && player instanceof ServerPlayer) {
            // With this call the server will request the client to open the appropriate Screenhandler
            ((ServerPlayer)player).openMenu(screenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(pos);
            } );
            player.awardStat(Statistics.STOVE_OPENED);
        }
    }
}
