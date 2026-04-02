package com.unlikepaladin.pfm.runtime.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;


import java.util.ArrayList;
import java.util.List;

import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.list;
import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.reloadSpritesOnClientThread;

@Mod.EventBusSubscriber(modid = "pfm", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TextureReloadQueueImpl {
    public static void registerTextureReload() {
        // quick check to avoid scheduling empty work
        if (list.isEmpty()) return;

        Minecraft.getInstance().execute(() -> {
            List<ResourceLocation> spriteIdentifiers = new ArrayList<>(list);
            list.clear();
            reloadSpritesOnClientThread(spriteIdentifiers);
        });
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        registerTextureReload();

    }
}
