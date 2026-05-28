package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PFMBookScreen extends Screen {
    public PFMBookScreen(Component title) {
        super(title);
    }

    public static final ResourceLocation BOOK_TEXTURE = ResourceLocation.fromNamespaceAndPath("pfm", "textures/gui/book_orange.png");
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderSystem.setShaderTexture(0, BOOK_TEXTURE);

        super.render(context, mouseX, mouseY, delta);
    }



}
