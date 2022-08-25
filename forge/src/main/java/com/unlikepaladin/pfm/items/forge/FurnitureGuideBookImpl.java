package com.unlikepaladin.pfm.items.forge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FurnitureGuideBookImpl {
    public static void openBook(ServerPlayerEntity user) {
        user.sendMessage(Text.of("You just tried to open the book"), true);
    }
}
