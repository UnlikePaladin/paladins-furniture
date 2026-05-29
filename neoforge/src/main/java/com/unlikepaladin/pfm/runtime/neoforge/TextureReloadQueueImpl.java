package com.unlikepaladin.pfm.runtime.neoforge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderFrameEvent;


import java.util.ArrayList;
import java.util.List;

import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.list;
import static com.unlikepaladin.pfm.runtime.TextureReloadQueue.reloadSpritesOnClientThread;

@EventBusSubscriber(modid = "pfm", value = Dist.CLIENT)
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
    public static void onClientTick(RenderFrameEvent.Pre event) {
        registerTextureReload();
    }
}
