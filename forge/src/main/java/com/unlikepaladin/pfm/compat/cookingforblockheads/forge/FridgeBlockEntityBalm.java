package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu.InventoryHandler;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FridgeBlockEntityBalm extends FridgeBlockEntity {
    private final InventoryHandler inventoryHandler;
    private final KitchenItemProvider itemProvider;
    private final LazyOptional<IItemHandler> inventoryHandlerCap;
    private final LazyOptional<IKitchenItemProvider> itemProviderCap;

    public FridgeBlockEntityBalm() {
        super();
        this.inventoryHandler = new InventoryHandler(this.inventory) {
            @Override
            protected void onContentsChanged(int slot) {
                FridgeBlockEntityBalm.this.markDirty();
                super.onContentsChanged(slot);
            }
        };
        this.itemProvider = new KitchenItemProvider(this.inventoryHandler);
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
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        LazyOptional<T> result = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, this.inventoryHandlerCap);
        if (!result.isPresent()) {
            result = CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, this.itemProviderCap);
        }

        return result.isPresent() ? result : super.getCapability(capability, facing);
    }
}
