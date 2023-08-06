package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.Direction;

import java.util.*;

public interface BlockEntityContract extends BalmProviderHolder {
    <T> T getProvider(Class<T> clazz);
    final Map<Class<?>, BalmProvider<?>> providers = new HashMap<>();

    default void buildProviders(List<BalmProviderHolder> providers) {
        providers.add(this);

        if (this instanceof BalmContainerProvider containerProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    Inventory container = containerProvider.getContainer();
                    if (container != null) {
                        return Lists.newArrayList(new BalmProvider<>(Inventory.class, container));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        Inventory container = containerProvider.getContainer(direction);
                        if (container != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(Inventory.class, container)));
                        }
                    }
                    return providers;
                }
            });
        }

        if (this instanceof BalmFluidTankProvider fluidTankProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    FluidTank fluidTank = fluidTankProvider.getFluidTank();
                    if (fluidTank != null) {
                        return Lists.newArrayList(new BalmProvider<>(FluidTank.class, fluidTank));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        FluidTank fluidTank = fluidTankProvider.getFluidTank(direction);
                        if (fluidTank != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(FluidTank.class, fluidTank)));
                        }
                    }
                    return providers;
                }
            });
        }

        if (this instanceof BalmEnergyStorageProvider energyStorageProvider) {
            providers.add(new BalmProviderHolder() {
                @Override
                public List<BalmProvider<?>> getProviders() {
                    EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage();
                    if (energyStorage != null) {
                        return Lists.newArrayList(new BalmProvider<>(EnergyStorage.class, energyStorage));
                    }

                    return Collections.emptyList();
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList<>();
                    for (Direction direction : Direction.values()) {
                        EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage(direction);
                        if (energyStorage != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(EnergyStorage.class, energyStorage)));
                        }
                    }
                    return providers;
                }
            });
        }
    }
}
