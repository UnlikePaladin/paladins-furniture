package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric;

import com.mojang.datafixers.util.Pair;
import com.unlikepaladin.pfm.blocks.blockentities.fabric.StovetopBlockEntityImpl;
import net.blay09.mods.balm.api.block.BalmBlockEntityContract;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenSmeltingProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class StovetopBlockEntityBalm extends StovetopBlockEntityImpl implements Container, IKitchenSmeltingProvider, BalmContainerProvider, BalmProviderHolder, BalmBlockEntityContract {
    private final DefaultKitchenItemProvider itemProvider;
    private final Map<Class<?>, BalmProvider<?>> providers = new HashMap<>();
    private final Map<Pair<Direction, Class<?>>, BalmProvider<?>> sidedProviders = new HashMap<>();
    private boolean providersInitialized;

    public StovetopBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new DefaultKitchenItemProvider(this);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    public List<BalmProvider<?>> getProviders() {
        return List.of(new BalmProvider<>(IKitchenItemProvider.class, this.itemProvider), new BalmProvider<>(IKitchenSmeltingProvider.class, this));
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
    public int getContainerSize() {
        return this.itemsBeingCooked.size();
    }

    @Override
    public boolean isEmpty() {
        return this.itemsBeingCooked.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.itemsBeingCooked.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack stack = ContainerHelper.removeItem(this.itemsBeingCooked, i, j);
        if (this.itemsBeingCooked.get(i).isEmpty()) {
            this.cookingTimes[i] = 0;
            this.cookingTotalTimes[i] = 0;
        }
        this.sendBlockUpdated();
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack arg) {
        this.itemsBeingCooked.set(i, arg);
        if (arg.isEmpty()) {
            this.cookingTimes[i] = 0;
            this.cookingTotalTimes[i] = 0;
        } else {
            this.cookingTimes[i] = 0;
            int cookTime = 200;
            if (this.level != null) {
                cookTime = this.level.getRecipeManager()
                    .getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(arg), this.level)
                    .map(CampfireCookingRecipe::getCookingTime)
                    .orElse(200);
            }
            this.cookingTotalTimes[i] = cookTime;
        }
        this.sendBlockUpdated();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) return false;
        if (this.level.getBlockEntity(this.worldPosition) != this) return false;
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
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