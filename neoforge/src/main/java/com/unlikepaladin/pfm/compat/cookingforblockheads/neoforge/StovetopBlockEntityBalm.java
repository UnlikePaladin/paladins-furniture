package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.neoforge.StovetopBlockEntityImpl;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.neoforge.energy.NeoForgeEnergyStorage;
import net.blay09.mods.balm.neoforge.fluid.NeoForgeFluidTank;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class StovetopBlockEntityBalm extends StovetopBlockEntityImpl implements Container, IKitchenSmeltingProvider, BalmContainerProvider, BalmProviderHolder, BlockEntityContract {

    public StovetopBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(IKitchenSmeltingProvider.class, this));
    }

    private boolean capabilitiesInitialized;
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!this.capabilitiesInitialized) {
            List<BalmProviderHolder> providers = new ArrayList<>();
            this.buildProviders(providers);

            for (BalmProviderHolder providerHolder : providers) {
                for (BalmProvider<?> provider : providerHolder.getProviders()) {
                    this.addCapabilities(provider, this.capabilities);
                }

                for(Pair<Direction, BalmProvider<?>> providerPair : providerHolder.getSidedProviders()) {
                    Direction direction = providerPair.getFirst();
                    BalmProvider<?> provider = providerPair.getSecond();
                    Map<Capability<?>, LazyOptional<?>> sidedCapabilities = this.sidedCapabilities.column(direction);
                    this.addCapabilities(provider, sidedCapabilities);
                }
            }

            this.capabilitiesInitialized = true;
        }

        LazyOptional<?> result = null;
        if (side != null) {
            result = this.sidedCapabilities.get(cap, side);
        }

        if (result == null) {
            result = this.capabilities.get(cap);
        }

        return result != null ? result.cast() : super.getCapability(cap, side);
    }

    private final Map<Capability<?>, LazyOptional<?>> capabilities = new HashMap<>();
    private final Table<Capability<?>, Direction, LazyOptional<?>> sidedCapabilities = HashBasedTable.create();
    private void addCapabilities(BalmProvider<?> provider, Map<Capability<?>, LazyOptional<?>> capabilities) {
        NeoForgeBalmProviders forgeProviders = (NeoForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        Objects.requireNonNull(provider);
        capabilities.put(capability, LazyOptional.of(provider::getInstance));
        if (provider.getProviderClass() == Container.class) {
            capabilities.put(Capabilities.ITEM_HANDLER, LazyOptional.of(() -> new InvWrapper((Container)provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(Capabilities.FLUID_HANDLER, LazyOptional.of(() -> new NeoForgeFluidTank((FluidTank)provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(Capabilities.ENERGY, LazyOptional.of(() -> new NeoForgeEnergyStorage((EnergyStorage)provider.getInstance())));
        }
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        int firstEmptyProcessing = -1;
        int processingStart = 0;
        for (int i = processingStart; i < this.itemsBeingCooked.size(); i++) {
            if (getItem(i).isEmpty()) { firstEmptyProcessing = i; break; }
        }

        if (firstEmptyProcessing != -1) {
            if (!itemStack.isEmpty()) {
                ItemStack moved = itemStack.split(1);
                setItem(firstEmptyProcessing, moved);
                return itemStack.isEmpty() ? ItemStack.EMPTY : itemStack;
            }
        }

        return itemStack;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(KitchenItemProvider.class, this.itemProvider));
    }
}