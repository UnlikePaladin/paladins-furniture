package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.menus.AbstractFreezerScreenHandler;
import com.unlikepaladin.pfm.menus.FreezerScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FreezerScreen extends HandledScreen<AbstractFreezerScreenHandler> {
    private final Identifier background = new Identifier(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
    private boolean narrow;

    public FreezerScreen(AbstractFreezerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.narrow = this.width < 379;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
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
        int k;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.client.getTextureManager().bindTexture(this.background);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (((AbstractFreezerScreenHandler)this.handler).isBurning()) {
            k = ((AbstractFreezerScreenHandler)this.handler).getFuelProgress();
            this.drawTexture(matrices, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
        k = ((AbstractFreezerScreenHandler)this.handler).getCookProgress();
        this.drawTexture(matrices, i + 79, j + 34, 176, 14, k + 1, 16);
    }

}
