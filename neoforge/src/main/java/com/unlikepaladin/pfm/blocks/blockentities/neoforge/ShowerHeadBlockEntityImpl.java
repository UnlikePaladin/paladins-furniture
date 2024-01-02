package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.ShowerHeadBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShowerHeadBlockEntityImpl extends ShowerHeadBlockEntity {
    public ShowerHeadBlockEntityImpl(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        return this.saveInitialChunkData(new NbtCompound());
    }

    @Override
    public void handleUpdateTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.isOpen = pkt.getNbt().getBoolean("isOpen");
    }

    public static BlockEntityType.BlockEntityFactory<? extends ShowerHeadBlockEntity> getFactory() {
        return ShowerHeadBlockEntityImpl::new;
    }
}
