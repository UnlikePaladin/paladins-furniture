package com.unlikepaladin.pfm.menus.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.neoforge.MicrowaveActivePacket;
import com.unlikepaladin.pfm.registry.neoforge.NetworkRegistryNeoForge;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class AbstractMicrowaveScreenHandlerImpl {
    public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
        microwaveBlockEntity.isActive = isActive;
        BlockPos pos = microwaveBlockEntity.getBlockPos();
        MicrowaveActivePacket activePacket = new MicrowaveActivePacket(pos, isActive);
        PacketDistributor.SERVER.noArg().send(activePacket);
    }
}
