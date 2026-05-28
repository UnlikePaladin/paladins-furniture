package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.storage.ReadView;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LampBlockEntityImpl extends LampBlockEntity {
    public LampBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static BlockEntityType.BlockEntitySupplier<? extends LampBlockEntity> getFactory() {
        return LampBlockEntityImpl::new;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbt = super.getUpdateTag(registryLookup);
        nbt.putString("color", this.color.getSerializedName());
        nbt.putString("variant", this.variant.getIdentifier().toString());
        return nbt;
    }

    @Override
    public void handleUpdateTag(ReadView view, HolderLookup.Provider holders) {
        this.loadAdditional(view, holders);
        this.readData(view);
    }

    @Override
    public void onDataPacket(Connection net, ReadView data, HolderLookup.Provider lookup) {
        super.onDataPacket(net, data, lookup);
        this.color = DyeColor.byName(data.getString("color", "white"), DyeColor.WHITE);
        this.variant = WoodVariantRegistry.getVariant(ResourceLocation.tryParse(data.getString("variant", "minecraft:oak")));
    }

}
