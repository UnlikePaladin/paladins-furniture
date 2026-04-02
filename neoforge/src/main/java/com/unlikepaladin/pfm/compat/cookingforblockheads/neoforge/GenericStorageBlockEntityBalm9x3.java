package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;

import java.util.*;

public class GenericStorageBlockEntityBalm9x3 extends GenericStorageBlockEntity9x3 implements BalmContainerProvider, BalmProviderHolder {
    public final KitchenItemProvider itemProvider;

    public GenericStorageBlockEntityBalm9x3(BlockPos pos, BlockState state) {
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
