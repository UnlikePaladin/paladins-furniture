package com.unlikepaladin.pfm.blocks.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MicrowaveBlockImpl {
    public static BlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return new MicrowaveBlockEntityImpl(pos, state);
    }

    public static void openScreen(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        if (world.isChunkLoaded(pos) && world.getBlockEntity(pos) instanceof MicrowaveBlockEntityImpl microwaveBlockEntity){
            NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory(((syncId, inv, player1) -> new MicrowaveScreenHandler(microwaveBlockEntity, syncId, inv, microwaveBlockEntity, new MicrowavePropertyDelegate(microwaveBlockEntity, 2))), Text.translatable("container.pfm.microwave"));
            if (player instanceof ServerPlayerEntity) {
                player.openMenu(namedScreenHandlerFactory, packetByteBuf -> {
                    packetByteBuf.writeBlockPos(microwaveBlockEntity.getPos());
                    packetByteBuf.writeBoolean(microwaveBlockEntity.isActive);
                } );
            }
        }
    }
}
