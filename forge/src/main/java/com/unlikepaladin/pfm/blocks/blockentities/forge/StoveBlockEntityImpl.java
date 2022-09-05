package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoveBlockEntityImpl extends StoveBlockEntity {

    public StoveBlockEntityImpl() {
        super();
    }

    public StoveBlockEntityImpl(BlockEntityType<?> entity) {
        super(entity);
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataNbt());
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt =  this.saveInitialChunkData(new NbtCompound());
        Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
        return nbt;
    }

    public void handleUpdateTag(NbtCompound tag) {
        this.fromTag(getCachedState(), tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.itemsBeingCooked.clear();
        Inventories.readNbt(pkt.getNbt(), this.itemsBeingCooked);
    }

}
