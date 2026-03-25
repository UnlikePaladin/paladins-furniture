package com.unlikepaladin.pfm.blocks.blockentities.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import com.unlikepaladin.pfm.compat.sandwichable.PFMSandwichableCompat;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

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
    public void fromClientTag(CompoundTag CompoundTag) {
        this.load(CompoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag CompoundTag) {
        return this.save(CompoundTag);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }
}
