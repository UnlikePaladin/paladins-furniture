package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PFMBookScreen extends Screen {
    public PFMBookScreen(Text title) {
        super(title);
    }

    public static final Identifier BOOK_TEXTURE = new Identifier("pfm", "textures/gui/book_orange.png");
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        MinecraftClient.getInstance().getTextureManager().bindTexture(BOOK_TEXTURE);

        super.render(matrices, mouseX, mouseY, delta);
    }



}
