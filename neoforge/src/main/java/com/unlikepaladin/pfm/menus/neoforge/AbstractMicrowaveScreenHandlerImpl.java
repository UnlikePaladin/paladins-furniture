package com.unlikepaladin.pfm.menus.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.MicrowaveActivatePayload;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class AbstractMicrowaveScreenHandlerImpl {
    public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
        microwaveBlockEntity.isActive = isActive;
        BlockPos pos = microwaveBlockEntity.getPos();
        MicrowaveActivatePayload activePacket = new MicrowaveActivatePayload(pos, isActive);
        PacketDistributor.sendToServer(activePacket);
    }
}
