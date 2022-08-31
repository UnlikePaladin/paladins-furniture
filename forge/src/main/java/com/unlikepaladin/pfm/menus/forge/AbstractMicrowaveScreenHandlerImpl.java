package com.unlikepaladin.pfm.menus.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.client.forge.ClientPacketsForge;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import net.minecraft.util.math.BlockPos;

public class AbstractMicrowaveScreenHandlerImpl {
    public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
        microwaveBlockEntity.isActive = isActive;
        BlockPos pos = microwaveBlockEntity.getPos();
        MicrowaveActivePacket activePacket = new MicrowaveActivePacket(pos, isActive);
        ClientPacketsForge.PFM_CLIENT_CHANNEL.sendToServer(activePacket);
    }
}
