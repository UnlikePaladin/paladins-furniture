package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableCompat;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity implements BlockEntityClientSerializable {
    public PFMToasterBlockEntityImpl() {
        super();
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
        PFMSandwichableCompat.toastSandwich(pfmToasterBlockEntity);
    }

    public static boolean isMetal(ItemStack stack) {
        return PFMSandwichableCompat.isMetal(stack);
    }

    @Override
    public void fromClientTag(NbtCompound NbtCompound) {
        this.fromTag(this.getCachedState(), NbtCompound);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound NbtCompound) {
        return this.writeNbt(NbtCompound);
    }

    public static Supplier<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }
}
