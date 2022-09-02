package com.unlikepaladin.pfm.menus.forge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.forge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.registry.forge.NetworkRegistryForge;
import net.minecraft.util.math.BlockPos;

public class AbstractMicrowaveScreenHandlerImpl {
    public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
        microwaveBlockEntity.isActive = isActive;
        BlockPos pos = microwaveBlockEntity.getPos();
        MicrowaveActivePacket activePacket = new MicrowaveActivePacket(pos, isActive);
        NetworkRegistryForge.PFM_CHANNEL.sendToServer(activePacket);
    }
}
