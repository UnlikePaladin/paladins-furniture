package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.OvenBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.InventoryHandler;
import com.unlikepaladin.pfm.registry.BlockEntities;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

public class OvenBlockEntityBalm extends OvenBlockEntity implements IKitchenSmeltingProvider {
    private final DefaultKitchenItemProvider itemProvider;
    private final InventoryHandler inventoryHandler;
    private final LazyOptional<IItemHandler> itemHandlerCap;
    private final LazyOptional<IKitchenItemProvider> itemProviderCap;
    private final LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap;

    public OvenBlockEntityBalm() {
        super(BlockEntities.KITCHEN_COUNTER_OVEN_BLOCK_ENTITY);
        this.inventoryHandler = new InventoryHandler(this.items) {
            @Override
            protected void onContentsChanged(int slot) {
                OvenBlockEntityBalm.this.setChanged();
                super.onContentsChanged(slot);
            }
        };
        this.itemProvider = new KitchenItemProvider(inventoryHandler);
        this.itemHandlerCap = LazyOptional.of(() -> this.inventoryHandler);
        this.itemProviderCap = LazyOptional.of(() -> this.itemProvider);
        this.smeltingProviderCap = LazyOptional.of(() -> this);
    }

    public OvenBlockEntityBalm(BlockEntityType<? extends OvenBlockEntity> type) {
        super(type);
        this.inventoryHandler = new InventoryHandler(this.items) {
            @Override
            protected void onContentsChanged(int slot) {
                OvenBlockEntityBalm.this.setChanged();
                super.onContentsChanged(slot);
            }
        };
        this.itemProvider = new KitchenItemProvider(inventoryHandler);
        this.itemHandlerCap = LazyOptional.of(() -> this.inventoryHandler);
        this.itemProviderCap = LazyOptional.of(() -> this.itemProvider);
        this.smeltingProviderCap = LazyOptional.of(() -> this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.load(this.getBlockState(), pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tagCompound = super.getUpdateTag();
        this.save(tagCompound);
        return tagCompound;
    }


    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, this.itemHandlerCap);
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, this.itemProviderCap);
        } else if (capability == CapabilityKitchenSmeltingProvider.CAPABILITY) {
            return this.smeltingProviderCap.cast();
        }
        return result.isPresent() ? result : super.getCapability(capability, facing);
    }

    @Override
    public ItemStack smeltItem(ItemStack itemStack) {
        int firstEmptyProcessing = -1;
        int processingStart = INPUT_COUNT;
        int processingEnd = processingStart + PROCESSING_COUNT;
        for (int i = processingStart; i < processingEnd; i++) {
            if (getItem(i).isEmpty()) {
                firstEmptyProcessing = i;
                break;
            }
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

    public IItemHandler getContainer() {
        return this.inventoryHandler;
    }
}