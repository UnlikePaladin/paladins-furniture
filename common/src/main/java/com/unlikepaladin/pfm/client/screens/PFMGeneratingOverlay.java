package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.runtime.PFMResourceProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;

public class PFMGeneratingOverlay extends Overlay {
    private long reloadCompleteTime = -1L;
    private long reloadStartTime = -1L;
    private final boolean reloading;

    PFMGeneratingOverlay(Overlay parent, PFMResourceProgress resourceProgress, MinecraftClient client, boolean reloading) {
        this.reloading = reloading;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }
    }
}
