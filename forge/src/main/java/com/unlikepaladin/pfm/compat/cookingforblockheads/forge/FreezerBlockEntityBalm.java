package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.FreezerBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.InventoryHandler;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IngredientPredicate;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import net.blay09.mods.cookingforblockheads.api.SourceItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nonnull;
import java.util.*;

public class FreezerBlockEntityBalm extends FreezerBlockEntity {
    private final InventoryHandler inventoryHandler;
    private final KitchenItemProvider itemProvider;
    private final LazyOptional<IItemHandler> inventoryHandlerCap;
    private final LazyOptional<IKitchenItemProvider> itemProviderCap;

    public FreezerBlockEntityBalm() {
        super();
        this.inventoryHandler = new InventoryHandler(this.inventory) {
            @Override
            protected void onContentsChanged(int slot) {
                FreezerBlockEntityBalm.this.markDirty();
                super.onContentsChanged(slot);
            }
        };
        this.itemProvider = new KitchenItemProvider(this.inventoryHandler){
            private final ItemStack snowStack;
            private final ItemStack iceStack;
            {
                this.snowStack = new ItemStack(Items.SNOWBALL);
                this.iceStack = new ItemStack(Blocks.ICE);
            }
            private @Nullable SourceItem applyIceUnit(IngredientPredicate predicate, int maxAmount) {
                if (predicate.test(this.snowStack, 64))
                    return new SourceItem(this, -1, ItemHandlerHelper.copyStackWithSize(this.snowStack, maxAmount));
                else
                    return predicate.test(this.iceStack, 64) ? new SourceItem(this, -1, ItemHandlerHelper.copyStackWithSize(this.iceStack, maxAmount)) : null;
            }
            public @Nullable SourceItem findSource(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
                SourceItem iceUnitResult = this.applyIceUnit(predicate, maxAmount);
                if (iceUnitResult != null) {
                    return iceUnitResult;
                } else {
                    return super.findSource(predicate, maxAmount, inventories, requireBucket, simulate);
                }
            }
            public @Nullable SourceItem findSourceAndMarkAsUsed(IngredientPredicate predicate, int maxAmount, List<IKitchenItemProvider> inventories, boolean requireBucket, boolean simulate) {
                SourceItem iceUnitResult = this.applyIceUnit(predicate, maxAmount);
                if (iceUnitResult != null) {                     return iceUnitResult;
                } else {
                    return super.findSourceAndMarkAsUsed(predicate, maxAmount, inventories, requireBucket, simulate);
                }
            }
        };
        this.inventoryHandlerCap = LazyOptional.of(() -> this.inventoryHandler);
        this.itemProviderCap = LazyOptional.of(() -> this.itemProvider);
    }


    public void fromTag(BlockState state, NbtCompound tagCompound) {
        super.fromTag(state, tagCompound);
        NbtCompound itemHandlerCompound = tagCompound.getCompound("ItemHandler");
        this.inventoryHandler.deserializeNBT(itemHandlerCompound);
    }

    public NbtCompound writeNbt(NbtCompound tagCompound) {
        super.writeNbt(tagCompound);
        tagCompound.put("ItemHandler", this.inventoryHandler.serializeNBT());
        return tagCompound;
    }

    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.fromTag(this.getCachedState(), pkt.getNbt());
    }

    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tagCompound = super.toInitialChunkDataNbt();
        this.writeNbt(tagCompound);
        return tagCompound;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @javax.annotation.Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, this.inventoryHandlerCap);
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, this.itemProviderCap);
        }

        return result.isPresent() ? result : super.getCapability(capability, facing);
    }
}