package com.unlikepaladin.pfm.blocks.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.MicrowaveBlockEntityImpl;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MicrowaveBlockImpl {
    public static BlockEntity getBlockEntity() {
        return new MicrowaveBlockEntityImpl();
    }

    public static void openScreen(PlayerEntity player, BlockState state, World world, BlockPos pos) {
        if (world.isChunkLoaded(pos) && world.getBlockEntity(pos) instanceof MicrowaveBlockEntityImpl){
            MicrowaveBlockEntityImpl microwaveBlockEntity = (MicrowaveBlockEntityImpl) world.getBlockEntity(pos);
            NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory(((syncId, inv, player1) -> new MicrowaveScreenHandler(microwaveBlockEntity, syncId, inv, microwaveBlockEntity, new MicrowavePropertyDelegate(microwaveBlockEntity, 2))), new TranslatableText("container.pfm.microwave"));
            NetworkHooks.openGui((ServerPlayerEntity) player, namedScreenHandlerFactory, packetByteBuf -> {
                packetByteBuf.writeBoolean(microwaveBlockEntity.isActive);
                packetByteBuf.writeBlockPos(microwaveBlockEntity.getPos());
            } );
        }
    }
}
