package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.TrashcanBlockEntityImpl;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class InnerTrashcanBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new TrashcanBlockEntityImpl(BlockEntities.TRASHCAN_BLOCK_ENTITY, pos, state);

    }

    public static void openScreen(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        if (world.isChunkLoaded(pos) && world.getBlockEntity(pos) instanceof TrashcanBlockEntityImpl){
            TrashcanBlockEntityImpl trashcanScreenHandler = (TrashcanBlockEntityImpl) world.getBlockEntity(pos);
            NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory(((syncId, inv, player1) -> new TrashcanScreenHandler(trashcanScreenHandler, syncId, inv, trashcanScreenHandler)), new TranslatableText("container.pfm.trashcan"));
            NetworkHooks.openGui((ServerPlayerEntity) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBlockPos(trashcanScreenHandler.getPos());
            } );
        }
    }
}
