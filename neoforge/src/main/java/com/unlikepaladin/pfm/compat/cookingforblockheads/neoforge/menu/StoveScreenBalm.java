package com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.menu;

import com.unlikepaladin.pfm.compat.cookingforblockheads.neoforge.StoveBlockEntityBalm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StoveScreenBalm extends AbstractContainerScreen<StoveScreenHandlerBalm> {
    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("cookingforblockheads", "textures/gui/oven.png");

    public StoveScreenBalm(StoveScreenHandlerBalm container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.imageWidth += 22;
        this.imageHeight = 193;
        this.titleLabelX += 22;
        this.inventoryLabelX += 22;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        this.renderTooltip(context, mouseX, mouseY);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        if (tileEntity.hasPowerUpgrade() && mouseX >= this.leftPos + this.imageWidth - 25 && mouseY >= this.topPos + 22 && mouseX < this.leftPos + this.imageWidth - 25 + 35 + 18 && mouseY < this.topPos + 22 + 72) {
            EnergyStorage energyStorage = tileEntity.getEnergyStorage();
            context.renderTooltip(Minecraft.getInstance().font, Component.translatable("tooltip.cookingforblockheads:energy_stored", energyStorage.getEnergy(), energyStorage.getCapacity()), mouseX, mouseY);
        }

    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        super.renderLabels(context, mouseX, mouseY);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();

        for(int i = 0; i < 9; ++i) {
            Slot slot = this.menu.slots.get(i + 7);
            if (slot.hasItem()) {
                ItemStack itemStack = tileEntity.getSmeltingResult(slot.getItem());
                if (!itemStack.isEmpty()) {
                }
            }
        }

    }

    @Override
    protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        context.blit(RenderType::getGuiTextured, texture, this.leftPos + 22, this.topPos, 0, 0, this.imageWidth - 22, this.imageHeight);
        context.blit(RenderType::getGuiTextured, texture, this.leftPos, this.topPos + 10, 176, 30, 25, 87);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;
        context.blit(RenderLayer::getGuiTextured, texture, this.leftPos + 22 + 61 + offsetX, this.topPos + 18, 176, 117, 76, 76, 256, 256);
        context.blit(RenderLayer::getGuiTextured, texture, this.leftPos + 22 + 38 + offsetX, this.topPos + 43, 205, 84, 18, 33, 256, 256);
        if (tileEntity.isBurning()) {
            int burnTime = (int)(12.0F * tileEntity.getBurnTimeProgress());
            context.blit(RenderLayer::getGuiTextured, texture, this.leftPos + 22 + 40 + offsetX, this.topPos + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1, 256, 256);
        }

        if (tileEntity.hasPowerUpgrade()) {
            context.blit(RenderLayer::getGuiTextured, texture, this.leftPos + this.imageWidth - 25, this.topPos + 22, 205, 0, 18, 72, 256, 256);
            EnergyStorage energyStorage = tileEntity.getEnergyStorage();
            float energyPercentage = (float)energyStorage.getEnergy() / (float)energyStorage.getCapacity();
            context.blit(RenderLayer::getGuiTextured, texture, this.leftPos + this.imageWidth - 25 + 1, this.topPos + 22 + 1 + 70 - (int)(energyPercentage * 70.0F), 223, 0, 16, (int)(energyPercentage * 70.0F), 256, 256);
        }

    }
}