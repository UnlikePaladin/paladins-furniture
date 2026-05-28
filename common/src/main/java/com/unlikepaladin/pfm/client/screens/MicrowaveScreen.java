package com.unlikepaladin.pfm.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.blocks.blockentities.MicrowaveBlockEntity;
import com.unlikepaladin.pfm.menus.AbstractMicrowaveScreenHandler;
import com.unlikepaladin.pfm.menus.MicrowaveScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import java.util.Optional;

public class MicrowaveScreen extends AbstractContainerScreen<MicrowaveScreenHandler> {
    private final ResourceLocation background = ResourceLocation.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"textures/gui/container/microwave.png");
    private boolean narrow;
    public boolean isActive;
    private MicrowaveBlockEntity microwaveBlockEntity;
    private RecipePropertySet recipePropertySet;

    private final Component startButtonText = Component.translatable("gui.pfm.microwave.start_button");
    public MicrowaveScreen(MicrowaveScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    public Button startButton;

    @Override
    public void init() {
        super.init();
        this.microwaveBlockEntity = menu.microwaveBlockEntity;
        isActive = menu.getActive();
        this.narrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.startButton = this.addRenderableWidget(new Button.Builder(startButtonText, button -> {
            AbstractMicrowaveScreenHandler.setActive(microwaveBlockEntity,true);
        }).pos(this.leftPos + 8, this.topPos + 40).size( 40, 20).build());
        if (recipePropertySet == null)
            recipePropertySet = microwaveBlockEntity.getLevel().recipeAccess().propertySet(RecipePropertySet.SMOKER_INPUT);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // if (this.recipeBook.isOpen() && this.narrow) {
        if (this.narrow) {
            this.renderBackground(context, mouseX, mouseY, delta);
            //this.recipeBook.render(context, mouseX, mouseY, delta);
        } else {
            //this.recipeBook.render(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
            //this.recipeBook.drawGhostSlots(context, this.x, this.y, true, delta);
        }
        this.renderTooltip(context, mouseX, mouseY);
        //this.recipeBook.drawTooltip(context, this.x, this.y, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int k;
        int i = this.leftPos;
        int j = this.topPos;
        context.blit(RenderType::guiTextured, this.background, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        k = this.menu.getCookProgress();
        k = Math.round(k * 1.75f);
        context.blit(RenderType::guiTextured, this.background, i + 147, j + 66 + -k, 176, 40 - k, 13, k +1, 256, 256);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.isActive = menu.isActive;

        if(!recipePropertySet.test(microwaveBlockEntity.getItem(0)) && !this.menu.isActive()) {
            this.startButton.active = false;
        }
        else {
            this.startButton.active = !this.menu.isActive();
        }
    }
}
