package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.menus.AbstractMicrowaveScreenHandler;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class TrashcanScreen extends HandledScreen<TrashcanScreenHandler> {
    private static final Identifier background = new Identifier("textures/gui/container/dispenser.png");
    private TrashcanBlockEntity trashcanBlockEntity;
    private boolean narrow;
    private ButtonWidget startButton;

    public TrashcanScreen(TrashcanScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
    private final TranslatableText startButtonText = new TranslatableText("gui.pfm.trashcan.clear_button");

    @Override
    public void init() {
        super.init();
        this.trashcanBlockEntity = handler.trashcanBlockEntity;
        this.narrow = this.width < 379;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.startButton = this.addDrawable(new ButtonWidget(this.x + 8, this.y + 40, 40, 20, startButtonText, button -> {
            TrashcanScreenHandler.clear(trashcanBlockEntity);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (this.narrow) {
            this.drawBackground(matrices, delta, mouseX, mouseY);
        } else {
            super.render(matrices, mouseX, mouseY, delta);
        }
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, background);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

}
