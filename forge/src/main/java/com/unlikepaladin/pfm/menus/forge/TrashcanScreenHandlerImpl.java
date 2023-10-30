package com.unlikepaladin.pfm.menus.forge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.networking.forge.TrashcanClearPacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        BlockPos pos = trashcanBlockEntity.getPos();
        TrashcanClearPacket clearPacket = new TrashcanClearPacket(pos);
        NetworkRegistryForge.PFM_CHANNEL.send(clearPacket, MinecraftClient.getInstance().getNetworkHandler().getConnection());
    }
}
