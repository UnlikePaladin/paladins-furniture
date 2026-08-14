package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.blocks.blockentities.TrashcanBlockEntity;
import com.unlikepaladin.pfm.menus.TrashcanScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.Identifier;

public class TrashcanScreen extends AbstractContainerScreen<TrashcanScreenHandler> {
    private static final Identifier background = Identifier.parse("textures/gui/container/dispenser.png");
    private TrashcanBlockEntity trashcanBlockEntity;
    private boolean narrow;
    private Button startButton;

    public TrashcanScreen(TrashcanScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    private static final Component startButtonText = Component.translatable("gui.pfm.trashcan.clear_button");

    @Override
    public void init() {
        super.init();
        this.trashcanBlockEntity = menu.trashcanBlockEntity;
        this.narrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.startButton = this.addRenderableWidget(new Button.Builder( startButtonText, button -> {
            TrashcanScreenHandler.clear(trashcanBlockEntity);
        }).pos(this.leftPos + 8, this.topPos + 40).size(40, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (this.narrow) {
            this.extractBackground(context, mouseX, mouseY, delta);
        } else {
            super.extractRenderState(context, mouseX, mouseY, delta);
        }
        this.extractTooltip(context, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, background, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

}
