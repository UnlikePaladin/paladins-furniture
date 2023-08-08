package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.PlateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlateBlockEntityImpl extends PlateBlockEntity {
    public PlateBlockEntityImpl() {
        super();
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataNbt());
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = this.saveInitialChunkData(new NbtCompound());
        Inventories.writeNbt(nbt, this.itemInPlate, true);
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, NbtCompound tag) {
        fromTag(state, tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.itemInPlate.clear();
        Inventories.readNbt(pkt.getNbt(), this.itemInPlate);
    }

    public static Supplier<? extends PlateBlockEntity> getFactory() {
        return PlateBlockEntityImpl::new;
    }
}
