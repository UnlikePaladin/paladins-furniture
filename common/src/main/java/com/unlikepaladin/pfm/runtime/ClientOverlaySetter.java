package com.unlikepaladin.pfm.runtime;

import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.mixin.PFMMinecraftClientAcccessor;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import org.joml.Matrix4fStack;

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

        Runnable runnable;
        while((runnable = ((PFMMinecraftClientAcccessor)client).getRenderTasks().poll()) != null) {
            runnable.run();
        }

        client.getMainRenderTarget().bindWrite(true);
        client.gameRenderer.render(client.getTimer(), shouldTick(client));
        client.getMainRenderTarget().unbindWrite();

        client.getMainRenderTarget().blitToScreen(client.getWindow().getWidth(), client.getWindow().getHeight());

        if (((PFMMinecraftClientAcccessor)client).getFrameCapturer() != null) {
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().upload();
            ((PFMMinecraftClientAcccessor)client).getFrameCapturer().capture(client.getFramebuffer());
        }
        client.getWindow().updateDisplay(((PFMMinecraftClientAcccessor)client).getFrameCapturer());
        ((DeltaTracker.Timer)client.getTimer()).updatePauseState(client.isPaused());
        ((DeltaTracker.Timer)client.getTimer()).updateFrozenState(!shouldTick(client));

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
