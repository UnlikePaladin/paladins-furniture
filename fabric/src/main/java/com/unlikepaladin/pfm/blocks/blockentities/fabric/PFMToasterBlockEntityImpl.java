package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.compat.fabric.sandwichable.PFMSandwichableCompat;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity implements BlockEntityClientSerializable {
    public PFMToasterBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
        PFMSandwichableCompat.toastSandwich(pfmToasterBlockEntity);
    }

    public static boolean isMetal(ItemStack stack) {
        return PFMSandwichableCompat.isMetal(stack);
    }

    @Override
    public void fromClientTag(NbtCompound NbtCompound) {
        this.readNbt(NbtCompound);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound NbtCompound) {
        return this.writeNbt(NbtCompound);
    }

    public static BlockEntityType.BlockEntityFactory<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }
}
