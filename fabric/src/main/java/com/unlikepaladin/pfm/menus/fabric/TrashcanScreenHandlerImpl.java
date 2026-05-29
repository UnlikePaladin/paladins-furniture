package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.networking.TrashcanClearPayload;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        BlockPos pos = trashcanBlockEntity.getBlockPos();
        ClientPlayNetworking.send(new TrashcanClearPayload(pos));
    }
}
