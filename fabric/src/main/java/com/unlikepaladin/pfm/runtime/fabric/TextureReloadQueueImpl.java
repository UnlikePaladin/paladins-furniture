package com.unlikepaladin.pfm.runtime.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.list;
import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.reloadSpritesOnClientThread;

public class TextureReloadQueueImpl {
    public static void registerTextureReload() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // quick check to avoid scheduling empty work
            if (list.isEmpty()) return;

            MinecraftClient.getInstance().execute(() -> {
                List<Identifier> spriteIdentifiers = new ArrayList<>(list);
                list.clear();
                reloadSpritesOnClientThread(spriteIdentifiers);
            });
        });
    }
}
