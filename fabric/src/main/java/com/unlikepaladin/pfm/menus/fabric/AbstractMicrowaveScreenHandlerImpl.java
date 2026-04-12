package com.unlikepaladin.pfm.menus.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.networking.MicrowaveActivatePayload;
import com.unlikepaladin.pfm.registry.NetworkIDs;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class AbstractMicrowaveScreenHandlerImpl {
        public static void setActive(MicrowaveBlockEntity microwaveBlockEntity, boolean isActive) {
            microwaveBlockEntity.isActive = isActive;
            BlockPos pos = microwaveBlockEntity.getBlockPos();
            // Send packet to server to change the block for us
            ClientPlayNetworking.send(new MicrowaveActivatePayload(pos, isActive));
        }
}
