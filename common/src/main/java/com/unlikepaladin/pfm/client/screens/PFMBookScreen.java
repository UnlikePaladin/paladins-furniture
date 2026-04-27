package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PFMBookScreen extends Screen {
    public PFMBookScreen(Component title) {
        super(title);
    }

    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation("pfm", "textures/gui/book_orange.png");
    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(BOOK_TEXTURE);

        super.render(matrices, mouseX, mouseY, delta);
    }



}
