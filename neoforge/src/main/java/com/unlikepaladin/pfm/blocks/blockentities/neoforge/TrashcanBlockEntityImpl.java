package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.ValueInput;
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
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.loadAdditional(input);
        super.handleUpdateTag(input);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(valueInput, this.inventory);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends TrashcanBlockEntity> getFactory() {
        return TrashcanBlockEntityImpl::new;
    }

}
