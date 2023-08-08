package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.GenericStorageBlockEntity9x3;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.forge.energy.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.fluid.ForgeFluidTank;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tile.CounterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GenericStorageBlockEntityBalm9x3 extends GenericStorageBlockEntity9x3 implements BalmContainerProvider, BalmProviderHolder, BalmBlockEntityContract {
    private final DefaultKitchenItemProvider itemProvider;

    public GenericStorageBlockEntityBalm9x3() {
        super();
        this.itemProvider = new DefaultKitchenItemProvider(this);
    }

    @Override
    public Inventory getContainer() {
        return this;
    }

    public List<BalmProvider<?>> getProviders() {
        return Lists.newArrayList(new BalmProvider[]{new BalmProvider(IKitchenItemProvider.class, this.itemProvider)});
    }

    private boolean capabilitiesInitialized;
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!this.capabilitiesInitialized) {
            List<BalmProviderHolder> providers = new ArrayList();
            this.balmBuildProviders(providers);
            Iterator var4 = providers.iterator();

            while(var4.hasNext()) {
                BalmProviderHolder providerHolder = (BalmProviderHolder)var4.next();
                Iterator var6 = providerHolder.getProviders().iterator();

                while(var6.hasNext()) {
                    BalmProvider<?> provider = (BalmProvider)var6.next();
                    this.addCapabilities(provider, this.capabilities);
                }

                var6 = providerHolder.getSidedProviders().iterator();

                while(var6.hasNext()) {
                    Pair<Direction, BalmProvider<?>> pair = (Pair)var6.next();
                    Direction direction = (Direction)pair.getFirst();
                    BalmProvider<?> provider = (BalmProvider)pair.getSecond();
                    Map<Capability<?>, LazyOptional<?>> sidedCapabilities = this.sidedCapabilities.column(direction);
                    this.addCapabilities(provider, sidedCapabilities);
                }
            }

            this.capabilitiesInitialized = true;
        }

        LazyOptional<?> result = null;
        if (side != null) {
            result = (LazyOptional)this.sidedCapabilities.get(cap, side);
        }

        if (result == null) {
            result = (LazyOptional)this.capabilities.get(cap);
        }

        return result != null ? result.cast() : super.getCapability(cap, side);
    }

    private final Map<Capability<?>, LazyOptional<?>> capabilities = new HashMap();
    private final Table<Capability<?>, Direction, LazyOptional<?>> sidedCapabilities = HashBasedTable.create();
    private void addCapabilities(BalmProvider<?> provider, Map<Capability<?>, LazyOptional<?>> capabilities) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        Objects.requireNonNull(provider);
        capabilities.put(capability, LazyOptional.of(provider::getInstance));
        if (provider.getProviderClass() == Inventory.class) {
            capabilities.put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(() -> new InvWrapper((Inventory)provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(() -> new ForgeFluidTank((FluidTank)provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(CapabilityEnergy.ENERGY, LazyOptional.of(() -> new ForgeEnergyStorage((EnergyStorage)provider.getInstance())));
        }

    }

    @Override
    public Box balmGetRenderBoundingBox() {
        return super.getRenderBoundingBox();
    }

    @Override
    public void balmOnLoad() {

    }

    @Override
    public void balmFromClientTag(NbtCompound nbtCompound) {
        return;
    }

    @Override
    public NbtCompound balmToClientTag(NbtCompound nbtCompound) {
        return nbtCompound;
    }

    @Override
    public void balmSync() {

    }
}
