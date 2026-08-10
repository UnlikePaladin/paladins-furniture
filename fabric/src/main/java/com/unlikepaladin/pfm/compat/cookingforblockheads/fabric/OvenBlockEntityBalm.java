package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.OvenBlockEntityImpl;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class OvenBlockEntityBalm extends OvenBlockEntityImpl implements IKitchenSmeltingProvider, BalmContainerProvider, BalmProviderHolder, BlockEntityContract {
    private final DefaultKitchenItemProvider itemProvider;
    private final Map<Class<?>, BalmProvider<?>> providers = new HashMap<>();
    private final Map<Pair<Direction, Class<?>>, BalmProvider<?>> sidedProviders = new HashMap<>();
    private boolean providersInitialized;

    public OvenBlockEntityBalm(BlockPos pos, BlockState state) {
        super(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY, pos, state);
        this.itemProvider = new DefaultKitchenItemProvider(new SubContainer(this, 12, 15));
    }

    public OvenBlockEntityBalm(BlockEntityType<? extends OvenBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.itemProvider = new DefaultKitchenItemProvider(new SubContainer(this, 12, 15));
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(IKitchenItemProvider.class, this.itemProvider), new BalmProvider<>(IKitchenSmeltingProvider.class, this));
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        int firstEmptyProcessing = -1;
        int processingStart = INPUT_COUNT;
        int processingEnd = processingStart + PROCESSING_COUNT;
        for (int i = processingStart; i < processingEnd; i++) {
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
    public int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            return CookingForBlockheadsConfig.getActive().ovenRequiresCookingOil && itemStack.is(Compat.getCookingOilTag()) ? 800 : Balm.getHooks().getBurnTime(itemStack);
        }
    }

    @Override
    public <T> T getProvider(Class<T> clazz) {
        if (!this.providersInitialized) {
            List<BalmProviderHolder> providers = new ArrayList<>();
            this.buildProviders(providers);

            for(BalmProviderHolder providerHolder : providers) {
                for(BalmProvider<?> provider : providerHolder.getProviders()) {
                    this.providers.put(provider.getProviderClass(), provider);
                }

                for(Pair<Direction, BalmProvider<?>> pair : providerHolder.getSidedProviders()) {
                    Direction direction = pair.getFirst();
                    BalmProvider<?> provider = pair.getSecond();
                    this.sidedProviders.put(Pair.of(direction, provider.getProviderClass()), provider);
                }
            }

            this.providersInitialized = true;
        }

        BalmProvider<?> found = this.providers.get(clazz);
        return (T)(found != null ? found.getInstance() : null);
    }
}