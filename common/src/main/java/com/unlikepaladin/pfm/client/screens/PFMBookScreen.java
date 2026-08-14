package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PFMBookScreen extends Screen {
    public PFMBookScreen(Component title) {
        super(title);
    }

    public static final Identifier BOOK_TEXTURE = Identifier.fromNamespaceAndPath("pfm", "textures/gui/book_orange.png");

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractBackground(graphics, mouseX, mouseY, a);
        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderSystem.setShaderTexture(0, BOOK_TEXTURE);

        super.extractRenderState(graphics, mouseX, mouseY, a);
    }



}
