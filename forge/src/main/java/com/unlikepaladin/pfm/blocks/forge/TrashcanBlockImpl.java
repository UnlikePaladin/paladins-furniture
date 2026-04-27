package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.TrashcanBlockEntityImpl;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class TrashcanBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new TrashcanBlockEntityImpl(BlockEntities.TRASHCAN_BLOCK_ENTITY);
    }

    public static void openScreen(Player player, BlockState state, Level world, BlockPos pos) {
        if (world.hasChunkAt(pos) && world.getBlockEntity(pos) instanceof TrashcanBlockEntityImpl){
            TrashcanBlockEntityImpl trashcanScreenHandler = (TrashcanBlockEntityImpl) world.getBlockEntity(pos);
            MenuProvider namedScreenHandlerFactory = new SimpleMenuProvider(((containerId, inv, player1) -> new TrashcanScreenHandler(trashcanScreenHandler, containerId, inv, trashcanScreenHandler)), new TranslatableComponent("container.pfm.trashcan"));
            NetworkHooks.openGui((ServerPlayer) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(trashcanScreenHandler.getBlockPos());
            } );
        }
    }
}
