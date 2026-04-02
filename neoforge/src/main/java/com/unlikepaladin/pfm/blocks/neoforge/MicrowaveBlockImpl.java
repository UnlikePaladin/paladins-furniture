package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MicrowaveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntityImpl(pos, state);
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof MicrowaveBlockEntityImpl microwaveBlockEntity){
            MenuProvider namedScreenHandlerFactory = new SimpleMenuProvider(((syncId, inv, player1) -> new MicrowaveScreenHandler(microwaveBlockEntity, syncId, inv, microwaveBlockEntity, new MicrowavePropertyDelegate(microwaveBlockEntity, 2))), Component.translatable("container.pfm.microwave"));
            if (player instanceof ServerPlayer) {
                player.openMenu(namedScreenHandlerFactory, packetByteBuf -> {
                    packetByteBuf.writeBoolean(microwaveBlockEntity.isActive);
                    packetByteBuf.writeBlockPos(microwaveBlockEntity.getBlockPos());
                } );
            }
        }
    }
}
