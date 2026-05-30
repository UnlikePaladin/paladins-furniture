package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LampBlockEntityImpl extends LampBlockEntity {
    public LampBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends LampBlockEntity> getFactory() {
        return LampBlockEntityImpl::new;
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
        super.handleUpdateTag(input);
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        super.onDataPacket(net, valueInput);
    }
}
