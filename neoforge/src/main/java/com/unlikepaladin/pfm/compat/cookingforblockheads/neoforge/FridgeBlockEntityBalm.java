package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.google.common.collect.Lists;
import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;

import java.util.List;

public class FridgeBlockEntityBalm extends FridgeBlockEntity implements BalmContainerProvider, BalmProviderHolder {

    public final KitchenItemProvider itemProvider;

    public FridgeBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(KitchenItemProvider.class, this.itemProvider));
    }
}
