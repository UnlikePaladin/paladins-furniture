package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class TrashcanBlockEntityImpl extends TrashcanBlockEntity {
    public TrashcanBlockEntityImpl(BlockEntityType<? extends TrashcanBlockEntity> trashcanBlockEntity, BlockPos pos, BlockState state) {
        super(trashcanBlockEntity, pos, state);
    }

    public TrashcanBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 13, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        ContainerHelper.saveAllItems(nbt, this.inventory);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
        super.handleUpdateTag(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pkt.getTag(), this.inventory);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends TrashcanBlockEntity> getFactory() {
        return TrashcanBlockEntityImpl::new;
    }

}
