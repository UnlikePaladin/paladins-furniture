package com.unlikepaladin.pfm.blocks.blockentities.forge;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity{
    public PFMToasterBlockEntityImpl() {
        super();
    }

    public static boolean isMetal(ItemStack stack) {
        return stack.getTranslationKey().contains("iron");
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
    }

    public static Supplier<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataNbt());
    }

    protected NbtCompound saveInitialChunkData(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items, true);
        return nbt;
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        return this.saveInitialChunkData(new NbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, NbtCompound tag) {
        this.fromTag(state, tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.getItems().clear();
        Inventories.readNbt(pkt.getNbt(), this.items);
    }
}
