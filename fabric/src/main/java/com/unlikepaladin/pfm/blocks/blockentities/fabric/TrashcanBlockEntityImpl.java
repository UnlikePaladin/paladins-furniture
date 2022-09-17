package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity implements ExtendedScreenHandlerFactory {
    public TrashcanBlockEntityImpl(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity) {
        super(trashcanBlockEntity);
    }
    public TrashcanBlockEntityImpl() {
        super();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(this.pos);
    }
}
