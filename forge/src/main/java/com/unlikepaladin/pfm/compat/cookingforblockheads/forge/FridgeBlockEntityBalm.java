package com.unlikepaladin.pfm.compat.cookingforblockheads.forge;

import com.unlikepaladin.pfm.blocks.blockentities.FridgeBlockEntity;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.capability.KitchenItemProviderHolder;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FridgeBlockEntityBalm extends FridgeBlockEntity implements BalmContainerProvider, KitchenItemProviderHolder {
    private final KitchenItemProvider itemProvider;

    public FridgeBlockEntityBalm(BlockPos pos, BlockState state) {
        super(pos, state);
        this.itemProvider = new ContainerKitchenItemProvider(this);
    }

    @Override
    public Container getContainer() {
        return this;
    }

    @Override
    public KitchenItemProvider getKitchenItemProvider() {
        return itemProvider;
    }
}
