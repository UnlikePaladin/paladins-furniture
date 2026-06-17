package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.forge.StovetopBlockEntityImpl;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.InventoryHandler;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StovetopBlockEntityBalm extends StovetopBlockEntityImpl implements Container, IKitchenSmeltingProvider {
    private final InventoryHandler inventoryHandler;
    private final LazyOptional<IItemHandler> itemHandlerCap;
    private final LazyOptional<IKitchenSmeltingProvider> smeltingProviderCap;

    public StovetopBlockEntityBalm() {
        super();
        this.inventoryHandler = new InventoryHandler(this.itemsBeingCooked) {
            @Override
            protected void onContentsChanged(int slot) {
                StovetopBlockEntityBalm.this.setChanged();
                super.onContentsChanged(slot);
            }
        };

        this.itemHandlerCap = LazyOptional.of(() -> inventoryHandler);
        this.smeltingProviderCap = LazyOptional.of(() -> this);
    }

    @Override
    public Container getContainer() {
        return this;
    }


    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlerCap.cast();
        } else {
            return capability == CapabilityKitchenSmeltingProvider.CAPABILITY ? this.smeltingProviderCap.cast() : super.getCapability(capability, facing);
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
}