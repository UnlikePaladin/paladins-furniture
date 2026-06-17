package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.StovetopBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.balm.forge.energy.ForgeEnergyStorage;
import net.blay09.mods.balm.forge.fluid.ForgeFluidTank;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StovetopBlockEntityBalm extends StovetopBlockEntityImpl implements Container, IKitchenSmeltingProvider, BalmContainerProvider, BalmProviderHolder, BalmBlockEntityContract {

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
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders) Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(provider.getProviderClass());
        Objects.requireNonNull(provider);
        capabilities.put(capability, LazyOptional.of(provider::getInstance));
        if (provider.getProviderClass() == Container.class) {
            capabilities.put(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, LazyOptional.of(() -> new InvWrapper((Container)provider.getInstance())));
        } else if (provider.getProviderClass() == FluidTank.class) {
            capabilities.put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, LazyOptional.of(() -> new ForgeFluidTank((FluidTank)provider.getInstance())));
        } else if (provider.getProviderClass() == EnergyStorage.class) {
            capabilities.put(CapabilityEnergy.ENERGY, LazyOptional.of(() -> new ForgeEnergyStorage((EnergyStorage)provider.getInstance())));
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
    public <T> T getProvider(Class<T> clazz) {
        ForgeBalmProviders forgeProviders = (ForgeBalmProviders)Balm.getProviders();
        Capability<?> capability = forgeProviders.getCapability(clazz);
        return (T) this.getCapability(capability).resolve().orElse(null);
    }
}