package com.unlikepaladin.pfm.menus.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.networking.TrashcanClearPayload;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;

public class TrashcanScreenHandlerImpl {
    public static void clear(TrashcanBlockEntity trashcanBlockEntity) {
        BlockPos pos = trashcanBlockEntity.getPos();
        TrashcanClearPayload clearPacket = new TrashcanClearPayload(pos);
        PacketDistributor.sendToServer(clearPacket);
    }
}
