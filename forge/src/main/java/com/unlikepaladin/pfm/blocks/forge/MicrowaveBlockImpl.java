package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class MicrowaveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntityImpl(pos, state);
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof MicrowaveBlockEntityImpl microwaveBlockEntity){
            MenuProvider namedScreenHandlerFactory = new SimpleMenuProvider(((containerId, inv, player1) -> new MicrowaveScreenHandler(microwaveBlockEntity, containerId, inv, microwaveBlockEntity, new MicrowavePropertyDelegate(microwaveBlockEntity, 2))), new TranslatableComponent("container.pfm.microwave"));
            NetworkHooks.openGui((ServerPlayer) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBoolean(microwaveBlockEntity.isActive);
                packetByteBuf.writeBlockPos(microwaveBlockEntity.getBlockPos());
            } );
        }
    }
}
