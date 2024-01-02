package com.unlikepaladin.pfm.blocks.blockentities.neoforge;

import com.unlikepaladin.pfm.blocks.blockentities.PFMToasterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PFMToasterBlockEntityImpl extends PFMToasterBlockEntity{
    public PFMToasterBlockEntityImpl(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public static boolean isMetal(ItemStack stack) {
        return stack.getTranslationKey().contains("iron");
    }

    public static void sandwichableToast(PFMToasterBlockEntity pfmToasterBlockEntity) {
    }

    public static BlockEntityType.BlockEntityFactory<? extends PFMToasterBlockEntity> getFactory() {
        return PFMToasterBlockEntityImpl::new;
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
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
    public void handleUpdateTag(NbtCompound tag) {
        this.readNbt(tag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket pkt) {
        super.onDataPacket(net, pkt);
        this.getItems().clear();
        Inventories.readNbt(pkt.getNbt(), this.items);
    }
}
