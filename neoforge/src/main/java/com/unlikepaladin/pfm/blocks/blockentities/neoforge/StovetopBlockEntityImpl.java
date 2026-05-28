package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StovetopBlockEntityImpl extends StovetopBlockEntity {
    public StovetopBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        return saveWithoutMetadata(lookup);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.loadAdditional(input);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
        this.itemsBeingCooked.clear();
        ContainerHelper.loadAllItems(valueInput, this.itemsBeingCooked);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends StovetopBlockEntity> getFactory() {
        return StovetopBlockEntityImpl::new;
    }
}
