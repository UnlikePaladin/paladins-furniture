package com.unlikepaladin.pfm.runtime;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
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

        Matrix4fStack matrixStack = RenderSystem.getModelViewStack();

        matrixStack.pushMatrix();
        RenderSystem.applyModelViewMatrix();
        client.getMainRenderTarget().bindWrite(true);
        long i = Util.getNanos();
        client.gameRenderer.render(1, i, false);
        client.getMainRenderTarget().unbindWrite();
        matrixStack.popMatrix();

        matrixStack.pushMatrix();
        RenderSystem.applyModelViewMatrix();
        client.getMainRenderTarget().blitToScreen(client.getWindow().getWidth(), client.getWindow().getHeight());
        matrixStack.popMatrix();

        RenderSystem.applyModelViewMatrix();
        client.getWindow().updateDisplay();
    }

}
