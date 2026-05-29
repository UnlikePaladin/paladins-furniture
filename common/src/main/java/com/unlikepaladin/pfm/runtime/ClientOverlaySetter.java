package com.unlikepaladin.pfm.runtime;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.client.PFMClientExtension;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.mixin.PFMMinecraftClientAcccessor;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

public class ClientOverlaySetter {
    public static void setOverlayToPFMOverlay(PFMResourceProgress resourceProgress) {
        if (!BlockItemRegistry.isModLoaded("vulkanmod")) {
            Minecraft client = Minecraft.getInstance();
            PFMGeneratingOverlay overlay = new PFMGeneratingOverlay(client.getOverlay(), resourceProgress, client, true);
            client.setOverlay(overlay);
        }
    }

    public static void updateScreen() {
        Minecraft client = Minecraft.getInstance();

        ((PFMClientExtension) client).invoke$runTasks();

        RenderSystem.executePendingTasks();

        RenderTarget framebuffer = client.getMainRenderTarget();
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(framebuffer.getColorTexture(), 0, framebuffer.getDepthTexture(), 1.0);

        client.gameRenderer.render(client.getDeltaTracker(), shouldTick(client));
        if (!client.getWindow().isMinimized())
            client.getMainRenderTarget().blitToScreen();

        if (((PFMMinecraftClientAcccessor)client).getFrameCapturer() != null) {
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().upload();
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().capture(client.getMainRenderTarget());
        }
        client.getWindow().updateDisplay(((PFMMinecraftClientAcccessor)client).getFrameCapturer());
        ((DeltaTracker.Timer)client.getDeltaTracker()).updatePauseState(client.isPaused());
        ((DeltaTracker.Timer)client.getDeltaTracker()).updateFrozenState(!shouldTick(client));

        client.getTextureManager().tick();
    }


    private static boolean shouldTick(Minecraft client) {
        return client.level == null || client.level.tickRateManager().runsNormally();
    }

    private static boolean resetLimiter = false;
    public static void setup() {
        if (Minecraft.getInstance().getFramerateLimitTracker() == null) {
            ((PFMMinecraftClientAcccessor)Minecraft.getInstance()).setInactivityFpsLimiter(new FramerateLimitTracker(Minecraft.getInstance().options, Minecraft.getInstance()));
            resetLimiter = true;
        }
    }

    public static void finish() {
        if (resetLimiter) {
            ((PFMMinecraftClientAcccessor)Minecraft.getInstance()).setInactivityFpsLimiter(null);
            resetLimiter = false;
        }
    }
}
