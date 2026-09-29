package com.unlikepaladin.pfm.runtime;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import com.unlikepaladin.pfm.client.PFMClientExtension;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.mixin.PFMMinecraftClientAcccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

public class ClientOverlaySetter {
    public static void setOverlayToPFMOverlay(PFMResourceProgress resourceProgress) {
        Minecraft client = Minecraft.getInstance();
        PFMGeneratingOverlay.registerTextures(client.getTextureManager());
        PFMGeneratingOverlay overlay = new PFMGeneratingOverlay(client.getOverlay(), resourceProgress, client, true);
        client.setOverlay(overlay);
    }

    public static void updateScreen() {
        Minecraft client = Minecraft.getInstance();

        ((PFMClientExtension) client).invoke$runTasks();

        ((PFMClientExtension) client).invoke$renderFrame(shouldTick(client));

        ((DeltaTracker.Timer) client.getDeltaTracker()).updatePauseState(client.isPaused());
        ((DeltaTracker.Timer) client.getDeltaTracker()).updateFrozenState(!shouldTick(client));

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
