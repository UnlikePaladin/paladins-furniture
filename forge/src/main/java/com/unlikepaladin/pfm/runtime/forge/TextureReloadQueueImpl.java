package com.unlikepaladin.pfm.runtime.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
            List<Identifier> spriteIdentifiers = new ArrayList<>(list);
            list.clear();
            reloadSpritesOnClientThread(spriteIdentifiers);
        });
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.PlayerTickEvent.Pre event) {
        registerTextureReload();
    }
}
