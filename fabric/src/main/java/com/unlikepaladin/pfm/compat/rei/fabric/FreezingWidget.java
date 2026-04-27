package com.unlikepaladin.pfm.compat.rei.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.widgets.BurningFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FreezingWidget extends BurningFire {
    private final ResourceLocation background = new ResourceLocation(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
    private Rectangle bounds;
    private double animationDuration = -1;

    public FreezingWidget(Rectangle bounds) {
        this.bounds = new Rectangle(Objects.requireNonNull(bounds));
    }

    @Override
    public double getAnimationDuration() {
        return animationDuration;
    }

    @Override
    public void setAnimationDuration(double animationDurationMS) {
        this.animationDuration = animationDurationMS;
        if (this.animationDuration <= 0)
            this.animationDuration = -1;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices, false, 1.0F);
    }

    public void renderBackground(PoseStack matrices, boolean dark, float alpha) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, alpha);
        Minecraft.getInstance().getTextureManager().bind(background);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendFunc(770, 771);
        if (getAnimationDuration() > 0) {
            int height = 14 - Mth.ceil((System.currentTimeMillis() / (animationDuration / 14) % 14d));
            //drawTexture(matrices, getX(), getY(), 1, 74, 14, 14 - height);
            blit(matrices, getX(), getY() +2, 56, 36,14, 14);
            blit(matrices, getX(), getY() + 14 - height, 176, 12 - height, 14, height);

        } else {
            blit(matrices, getX(), getY(), 1, 74, 14, 14);
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
