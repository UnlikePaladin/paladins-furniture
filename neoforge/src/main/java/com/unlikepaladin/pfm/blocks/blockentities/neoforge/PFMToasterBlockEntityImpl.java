package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity{
    public PFMToasterBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static boolean isMetal(ItemStack stack) {
        return stack.getItem().getDescriptionId().contains("iron");
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
    }

    public static BlockEntityType.BlockEntitySupplier<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
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
