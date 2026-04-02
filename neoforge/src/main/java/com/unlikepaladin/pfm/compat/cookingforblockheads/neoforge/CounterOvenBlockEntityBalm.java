package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.CounterOvenBlockEntity;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.neoforge.energy.NeoForgeEnergyStorage;
import net.blay09.mods.balm.neoforge.fluid.NeoForgeFluidTank;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CounterOvenBlockEntityBalm extends CounterOvenBlockEntity implements BalmContainerProvider, BalmProviderHolder {
    public final KitchenItemProvider itemProvider;

    public CounterOvenBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(KitchenItemProvider.class, this.itemProvider));
    }
}