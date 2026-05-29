package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;

public class CounterOvenBlockEntityBalm extends CounterOvenBlockEntity implements BalmContainerProvider, KitchenItemProviderHolder {
    private final KitchenItemProvider itemProvider;

    public CounterOvenBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }
}