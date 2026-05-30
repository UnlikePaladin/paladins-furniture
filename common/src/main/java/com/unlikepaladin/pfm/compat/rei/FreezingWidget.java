package com.unlikepaladin.pfm.compat.rei;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.BurningFire;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FreezingWidget extends BurningFire {
    private final Identifier background = Identifier.fromNamespaceAndPath(PaladinFurnitureMod.MOD_ID,"textures/gui/container/freezer.png");
    private Rectangle bounds;
    private double animationDuration = -1;
    private final NumberAnimator<Float> darkBackgroundAlpha = ValueAnimator.ofFloat()
            .withConvention(() -> REIRuntime.getInstance().isDarkThemeEnabled() ? 1.0F : 0.0F, ValueAnimator.typicalTransitionTime())
            .asFloat();

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
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.darkBackgroundAlpha.update(delta);
        renderBackground(context, false, 1.0F);
        renderBackground(context, true, this.darkBackgroundAlpha.value());
    }

    public void renderBackground(GuiGraphics context, boolean dark, float alpha) {
        if (getAnimationDuration() > 0) {
            int height = 14 - Mth.ceil((System.currentTimeMillis() / (animationDuration / 14) % 14d));
            //drawTexture(matrices, getX(), getY(), 1, 74, 14, 14 - height);
            context.blit(RenderPipelines.GUI_TEXTURED, background, getX(), getY() +2, 56, 36,14, 14, 256, 256);
            context.blit(RenderPipelines.GUI_TEXTURED, background, getX(), getY() + 14 - height, 176, 12 - height, 14, height, 256, 256);

        } else {
            context.blit(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), 1, 74, 14, 14, 256, 256);
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
