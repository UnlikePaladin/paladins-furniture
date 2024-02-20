package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity3x3;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class GenericStorageBlockEntityBalm3x3 extends GenericStorageBlockEntity3x3 implements BalmContainerProvider, BalmProviderHolder {
    public GenericStorageBlockEntityBalm3x3(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Inventory getContainer() {
        return this;
    }
}