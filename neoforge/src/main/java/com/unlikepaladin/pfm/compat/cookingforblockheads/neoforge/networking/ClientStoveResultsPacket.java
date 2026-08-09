package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.networking;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu.StoveScreenHandlerBalm;
import net.blay09.mods.cookingforblockheads.util.ListUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record ClientStoveResultsPacket(List<ItemStack> itemStacks) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientStoveResultsPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID, "stove_results"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientStoveResultsPacket> STREAM_CODEC;

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(Player player, ClientStoveResultsPacket packet) {
        AbstractContainerMenu var3 = player.containerMenu;
        if (var3 instanceof StoveScreenHandlerBalm ovenMenu) {
            ovenMenu.setResultItems(ListUtils.nonNullListOf(packet.itemStacks(), ItemStack.EMPTY));
        }
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ItemStack.OPTIONAL_LIST_STREAM_CODEC, ClientStoveResultsPacket::itemStacks, ClientStoveResultsPacket::new);
    }
}
