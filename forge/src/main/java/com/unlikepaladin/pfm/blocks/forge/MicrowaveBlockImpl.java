package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MicrowaveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntityImpl(pos, state);
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof MicrowaveBlockEntityImpl microwaveBlockEntity){
            MenuProvider namedScreenHandlerFactory = state.getMenuProvider(world, pos);
            if (player instanceof ServerPlayer) {
                // dear future me: order matters, if you put active before pos, it will not work correctly
                ((ServerPlayer)player).openMenu(namedScreenHandlerFactory, packetByteBuf -> {
                    packetByteBuf.writeBlockPos(microwaveBlockEntity.getBlockPos());
                    packetByteBuf.writeBoolean(microwaveBlockEntity.isActive);
                } );
            }
        }
    }
}
