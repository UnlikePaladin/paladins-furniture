package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableCompat;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.Nullable;;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity {
    public PFMToasterBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
        PFMSandwichableCompat.toastSandwich(pfmToasterBlockEntity);
    }

    public static boolean isMetal(ItemStack stack) {
        return PFMSandwichableCompat.isMetal(stack);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag toInitialChunkDataNbt() {
        return createNbt();
    }

    public static BlockEntityType.BlockEntitySupplier<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }
}
