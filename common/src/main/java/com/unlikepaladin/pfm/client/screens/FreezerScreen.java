package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class FreezerScreen extends AbstractContainerScreen<AbstractFreezerScreenHandler> {
    private final Identifier background = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
    private boolean narrow;

    public FreezerScreen(AbstractFreezerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.narrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (this.narrow) {
            this.extractBackground(graphics, mouseX, mouseY, a);
        } else {
            super.extractRenderState(graphics, mouseX, mouseY, a);
        }
        this.extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float a) {
        int k;
        int i = this.leftPos;
        int j = this.topPos;
        context.blit(RenderPipelines.GUI_TEXTURED, this.background, i, j, 0, 0, this.imageWidth, this.imageHeight,256, 256);
        if (this.menu.isActive()) {
            k = this.menu.getFuelProgress();
            context.blit(RenderPipelines.GUI_TEXTURED, this.background, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1, 256, 256);
        }
        k = this.menu.getFreezeProgress();
        context.blit(RenderPipelines.GUI_TEXTURED, this.background, i + 79, j + 34, 176, 14, k + 1, 16, 256, 256);
    }

}
