package com.unlikepaladin.pfm.runtime;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.client.PFMClientExtension;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.mixin.PFMMinecraftClientAcccessor;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.option.InactivityFpsLimiter;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.RenderTickCounter;

public class ClientOverlaySetter {
    public static void setOverlayToPFMOverlay(PFMResourceProgress resourceProgress) {
        if (!BlockItemRegistry.isModLoaded("vulkanmod")) {
            MinecraftClient client = MinecraftClient.getInstance();
            PFMGeneratingOverlay overlay = new PFMGeneratingOverlay(client.getOverlay(), resourceProgress, client, true);
            client.setOverlay(overlay);
        }
    }

    public static void updateScreen() {
        MinecraftClient client = MinecraftClient.getInstance();

        Runnable runnable;
        while((runnable = ((PFMMinecraftClientAcccessor)client).getRenderTasks().poll()) != null) {
            runnable.run();
        }

        ((PFMClientExtension) MinecraftClient.getInstance()).invoke$runTasks();

        Framebuffer framebuffer = client.getFramebuffer();
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(framebuffer.getColorAttachment(), 0, framebuffer.getDepthAttachment(), 1.0);
        RenderSystem.setShaderFog(Fog.DUMMY);

        client.gameRenderer.render(client.getRenderTickCounter(), shouldTick(client));
        client.getFramebuffer().blitToScreen();

        if (((PFMMinecraftClientAcccessor)client).getFrameCapturer() != null) {
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().upload();
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().capture(client.getFramebuffer());
        }
        client.getWindow().swapBuffers(((PFMMinecraftClientAcccessor)client).getFrameCapturer());
        ((RenderTickCounter.Dynamic)client.getRenderTickCounter()).tick(client.isPaused());
        ((RenderTickCounter.Dynamic)client.getRenderTickCounter()).setTickFrozen(!shouldTick(client));

        client.getTextureManager().tick();
    }


    private static boolean shouldTick(MinecraftClient client) {
        return client.world == null || client.world.getTickManager().shouldTick();
    }

    private static boolean resetLimiter = false;
    public static void setup() {
        if (MinecraftClient.getInstance().getInactivityFpsLimiter() == null) {
            ((PFMMinecraftClientAcccessor)MinecraftClient.getInstance()).setInactivityFpsLimiter(new InactivityFpsLimiter(MinecraftClient.getInstance().options, MinecraftClient.getInstance()));
            resetLimiter = true;
        }
    }

    public static void finish() {
        if (resetLimiter) {
            ((PFMMinecraftClientAcccessor)MinecraftClient.getInstance()).setInactivityFpsLimiter(null);
            resetLimiter = false;
        }
    }
}
