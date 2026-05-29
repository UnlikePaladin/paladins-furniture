package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;

public class GenericStorageBlockEntityBalm9x3 extends GenericStorageBlockEntity9x3 implements BalmContainerProvider, KitchenItemProviderHolder {
    private final KitchenItemProvider itemProvider;

    public GenericStorageBlockEntityBalm9x3(BlockPos pos, BlockState state) {
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
