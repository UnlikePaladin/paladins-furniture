package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableCompat;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

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
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static BlockEntityType.BlockEntityFactory<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }
}
