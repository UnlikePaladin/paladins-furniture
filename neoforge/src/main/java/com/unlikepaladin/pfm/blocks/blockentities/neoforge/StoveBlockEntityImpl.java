package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.StoveBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.StoveBlockEntityBalm;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;;

public class StoveBlockEntityImpl extends StoveBlockEntity {

    public StoveBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public StoveBlockEntityImpl(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
        super(entity, pos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbt =  this.saveInitialChunkData(new CompoundTag(), registryLookup);
        ContainerHelper.saveAllItems(nbt, this.itemsBeingCooked, true, registryLookup);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registryLookup) {
        this.loadAdditional(tag, registryLookup);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registryLookup) {
        super.onDataPacket(net, pkt, registryLookup);
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(pkt.getTag(), this.itemsBeingCooked, registryLookup);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends BlockEntity> getFactory() {
        return PaladinFurnitureMod.getModList().contains("cookingforblockheads") ? StoveBlockEntityBalm::new :StoveBlockEntityImpl::new;
    }
}
