package com.unlikepaladin.pfm.menus.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.networking.neoforge.TrashcanClearPacket;
import com.unlikepaladin.pfm.registry.neoforge.NetworkRegistryNeoForge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        BlockPos pos = trashcanBlockEntity.getPos();
        TrashcanClearPacket clearPacket = new TrashcanClearPacket(pos);
        NetworkRegistryNeoForge.PFM_CHANNEL.sendToServer(clearPacket);
    }
}
