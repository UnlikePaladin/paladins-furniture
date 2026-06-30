package com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.unlikepaladin.pfm.compat.cookingforblockheads.fabric.StoveBlockEntityBalm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.cookingforblockheads.block.entity.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.menu.OvenMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

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

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        this.renderTooltip(context, mouseX, mouseY);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        if (tileEntity.hasPowerUpgrade() && mouseX >= this.leftPos + this.width - 25 && mouseY >= this.topPos + 22 && mouseX < this.leftPos + this.width - 25 + 35 + 18 && mouseY < this.topPos + 22 + 72) {
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
                    PoseStack pose = context.pose();
                    pose.pushPose();
                    pose.translate(0.0F, 0.0F, 200.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, tileEntity.getCookProgress(i));
                    context.renderItem(itemStack, slot.x, slot.y);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    pose.popPose();
                }
            }
        }

    }

    @Override
    protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
        context.blit(RenderType::guiTextured, texture, this.leftPos + 22, this.topPos, 0.0F, 0.0F, this.imageWidth - 22, this.imageHeight, 256, 256);
        context.blit(RenderType::guiTextured, texture, this.leftPos, this.topPos + 10, 176.0F, 30.0F, 25, 87, 256, 256);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;
        context.blit(RenderType::guiTextured, texture, this.leftPos + 22 + 61 + offsetX, this.topPos + 18, 176.0F, 117.0F, 76, 76, 256, 256);
        context.blit(RenderType::guiTextured, texture, this.leftPos + 22 + 38 + offsetX, this.topPos + 43, 205.0F, 84.0F, 18, 33, 256, 256);
        if (tileEntity.isBurning()) {
            int burnTime = (int)(12.0F * tileEntity.getBurnTimeProgress());
            context.blit(RenderType::guiTextured, texture, this.leftPos + 22 + 40 + offsetX, this.topPos + 43 + 12 - burnTime, 176.0F, (float)(12 - burnTime), 14, burnTime + 1, 256, 256);
        }

        if (tileEntity.hasPowerUpgrade()) {
            context.blit(RenderType::guiTextured, texture, this.leftPos + this.imageWidth - 25, this.topPos + 22, 205.0F, 0.0F, 18, 72, 256, 256);
            EnergyStorage energyStorage = tileEntity.getEnergyStorage();
            float energyPercentage = (float)energyStorage.getEnergy() / (float)energyStorage.getCapacity();
            context.blit(RenderType::guiTextured, texture, this.leftPos + this.imageWidth - 25 + 1, this.topPos + 22 + 1 + 70 - (int)(energyPercentage * 70.0F), 223.0F, 0.0F, 16, (int)(energyPercentage * 70.0F), 256, 256);
        }

    }
}