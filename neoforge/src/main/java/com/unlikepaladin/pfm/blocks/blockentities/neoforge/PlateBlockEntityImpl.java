package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;;

public class PlateBlockEntityImpl extends PlateBlockEntity {
    public PlateBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = this.saveInitialChunkData(new NbtCompound());
        Inventories.writeNbt(nbt, this.itemInPlate, true);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.itemInPlate.clear();
        Inventories.readNbt(pkt.getNbt(), this.itemInPlate);
    }

    public static BlockEntityType.BlockEntityFactory<? extends PlateBlockEntity> getFactory() {
        return PlateBlockEntityImpl::new;
    }
}
