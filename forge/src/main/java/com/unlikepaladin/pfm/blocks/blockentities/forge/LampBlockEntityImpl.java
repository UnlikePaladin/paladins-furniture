package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.LampBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.data.materials.WoodVariantRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LampBlockEntityImpl extends LampBlockEntity {
    public LampBlockEntityImpl() {
        super();
    }

    public static Supplier<? extends LampBlockEntity> getFactory() {
        return LampBlockEntityImpl::new;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataNbt());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        nbt.putString("color", this.color.asString());
        nbt.putString("variant", this.variant.getIdentifier().toString());
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, NbtCompound tag) {
        this.fromTag(state, tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.color = DyeColor.byName(pkt.getNbt().getString("color"), DyeColor.WHITE);
        this.variant = WoodVariantRegistry.getVariant(Identifier.tryParse(pkt.getNbt().getString("variant")));
    }

}
