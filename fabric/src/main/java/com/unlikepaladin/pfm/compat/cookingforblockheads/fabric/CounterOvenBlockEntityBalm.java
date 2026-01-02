package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class CounterOvenBlockEntityBalm extends CounterOvenBlockEntity implements BalmContainerProvider, KitchenItemProviderHolder {
    private final KitchenItemProvider itemProvider;

    public CounterOvenBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this);
    }

    @Override
    public Inventory getContainer() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }
}