package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.unlikepaladin.pfm.menus.OvenScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class OvenScreen extends AbstractContainerScreen<OvenScreenHandler> {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse("pfm:textures/gui/container/oven.png");

    public OvenScreen(OvenScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 195;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.inventoryLabelY += 29;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float f, int i, int j) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, x+58, y+18, 176, 50, 76, 76, 256, 256);
        graphics.blit(RenderType::guiTextured, TEXTURE, x+36, y+43, 176, 17, 17, 33, 256, 256);

        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            graphics.blit(RenderType::guiTextured, TEXTURE, x + 36, y + 41 + (14 - litProgress), 176, 14 - litProgress, 14, litProgress + 1, 256, 256);
        }

        int gridX = this.leftPos + 58;
        int gridY = this.topPos + 40;

        for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
            int row = slotIndex / 3;
            int col = slotIndex % 3;

            int slotX = gridX + (col * 18);
            int slotY = gridY + (row * 18);

            if (this.menu.isSlotOverheating(slotIndex)) {
                // we are overcooking with this one
                int burnHeight = this.menu.getOverovercookProgress(slotIndex, 18);
                if (burnHeight > 0) {
                    graphics.blit(RenderType::guiTextured, TEXTURE, slotX, slotY + (18 - burnHeight), 212, 32 + (18 - burnHeight), 18, burnHeight, 256, 256);

                }
            } else {
                int cookHeight = this.menu.getCookProgress(slotIndex, 18);
                if (cookHeight > 0) {
                    graphics.blit(RenderType::guiTextured, TEXTURE, slotX, slotY + (18 - cookHeight), 194, 32 + (18 - cookHeight), 18, cookHeight, 256, 256);
                }
            }
        }
    }
}
