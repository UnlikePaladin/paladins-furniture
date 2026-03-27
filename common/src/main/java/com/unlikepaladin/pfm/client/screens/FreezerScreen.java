package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FreezerScreen extends AbstractContainerScreen<AbstractFreezerScreenHandler> {
    private final ResourceLocation background = new ResourceLocation(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
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
        int k;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, this.background);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrices, i, j, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isActive()) {
            k = this.menu.getFuelProgress();
            this.blit(matrices, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
        k = this.menu.getFreezeProgress();
        this.blit(matrices, i + 79, j + 34, 176, 14, k + 1, 16);
    }

}
