package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.neoforge.FreezerBlockEntityImpl;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class FreezerBlockEntityBalm extends FreezerBlockEntityImpl implements BalmContainerProvider, BalmProviderHolder {

    public FreezerBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Inventory getContainer() {
        return this;
    }
}