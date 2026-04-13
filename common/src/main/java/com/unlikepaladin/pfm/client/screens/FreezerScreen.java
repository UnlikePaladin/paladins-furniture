package com.unlikepaladin.pfm.client.screens;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FreezerScreen extends AbstractContainerScreen<AbstractFreezerScreenHandler> {
    private final ResourceLocation background = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
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
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (this.narrow) {
            this.renderBackground(context, mouseX, mouseY, delta);
        } else {
            super.render(context, mouseX, mouseY, delta);
        }
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int k;
        int i = this.leftPos;
        int j = this.topPos;
        context.drawTexture(RenderType::getGuiTextured, this.background, i, j, 0, 0, this.imageWidth, this.imageHeight,256, 256);
        if (this.menu.isActive()) {
            k = this.menu.getFuelProgress();
            context.drawTexture(RenderType::getGuiTextured, this.background, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1, 256, 256);
        }
        k = this.menu.getFreezeProgress();
        context.blit(RenderType::getGuiTextured, this.background, i + 79, j + 34, 176, 14, k + 1, 16, 256, 256);
    }

}
