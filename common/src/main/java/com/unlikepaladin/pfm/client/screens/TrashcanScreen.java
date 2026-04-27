package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class TrashcanScreen extends AbstractContainerScreen<TrashcanScreenHandler> {
    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/dispenser.png");
    private TrashcanBlockEntity trashcanBlockEntity;
    private boolean narrow;
    private Button startButton;

    public TrashcanScreen(TrashcanScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    private final TranslatableComponent startButtonText = new TranslatableComponent("gui.pfm.trashcan.clear_button");

    @Override
    public void init() {
        super.init();
        this.trashcanBlockEntity = menu.trashcanBlockEntity;
        this.narrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.startButton = this.addButton(new Button(this.leftPos + 8, this.topPos + 40, 40, 20, startButtonText, button -> {
            TrashcanScreenHandler.clear(trashcanBlockEntity);
        }));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        if (this.narrow) {
            this.renderBg(matrices, delta, mouseX, mouseY);
        } else {
            super.render(matrices, mouseX, mouseY, delta);
        }
        this.renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindForSetup(background);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrices, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

}
