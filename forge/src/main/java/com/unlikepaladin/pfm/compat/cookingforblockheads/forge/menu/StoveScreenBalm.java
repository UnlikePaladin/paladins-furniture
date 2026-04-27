package com.unlikepaladin.pfm.compat.cookingforblockheads.forge.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.compat.cookingforblockheads.forge.StoveBlockEntityBalm;
import net.blay09.mods.cookingforblockheads.tile.util.EnergyStorageModifiable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class StoveScreenBalm extends AbstractContainerScreen<StoveScreenHandlerBalm> {
    private static final ResourceLocation texture = new ResourceLocation("cookingforblockheads", "textures/gui/oven.png");

    public StoveScreenBalm(StoveScreenHandlerBalm container, Inventory playerInventory, Component displayName) {
        super(container, playerInventory, displayName);
        this.imageWidth += 22;
        this.imageHeight = 193;
        this.titleLabelX += 22;
        this.inventoryLabelX += 22;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        if (tileEntity.hasPowerUpgrade() && mouseX >= this.leftPos + this.imageWidth - 25 && mouseY >= this.topPos + 22 && mouseX < this.leftPos + this.imageWidth - 25 + 35 + 18 && mouseY < this.topPos + 22 + 72) {
            EnergyStorageModifiable energyStorage = tileEntity.getEnergyStorage();
            this.renderTooltip(poseStack, new TranslatableComponent("tooltip.cookingforblockheads:energy_stored", energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored()), mouseX, mouseY);
        }

    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
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

    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(texture);
        this.blit(poseStack, this.leftPos + 22, this.topPos, 0, 0, this.imageWidth - 22, this.imageHeight);
        this.blit(poseStack, this.leftPos, this.topPos + 10, 176, 30, 25, 87);
        StoveBlockEntityBalm tileEntity = this.menu.getTileEntity();
        int offsetX = tileEntity.hasPowerUpgrade() ? -5 : 0;
        this.blit(poseStack, this.leftPos + 22 + 61 + offsetX, this.topPos + 18, 176, 117, 76, 76);
        this.blit(poseStack, this.leftPos + 22 + 38 + offsetX, this.topPos + 43, 205, 84, 18, 33);
        if (tileEntity.isBurning()) {
            int burnTime = (int)(12.0F * tileEntity.getBurnTimeProgress());
            this.blit(poseStack, this.leftPos + 22 + 40 + offsetX, this.topPos + 43 + 12 - burnTime, 176, 12 - burnTime, 14, burnTime + 1);
        }

        if (tileEntity.hasPowerUpgrade()) {
            this.blit(poseStack, this.leftPos + this.imageWidth - 25, this.topPos + 22, 205, 0, 18, 72);
            EnergyStorageModifiable energyStorage = tileEntity.getEnergyStorage();
            float energyPercentage = (float)energyStorage.getEnergyStored() / (float)energyStorage.getMaxEnergyStored();
            this.blit(poseStack, this.leftPos + this.imageWidth - 25 + 1, this.topPos + 22 + 1 + 70 - (int)(energyPercentage * 70.0F), 223, 0, 16, (int)(energyPercentage * 70.0F));
        }

    }
}