package com.unlikepaladin.pfm.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.unlikepaladin.pfm.client.screens.overlay.GLText;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Closeable;

import static com.unlikepaladin.pfm.client.screens.overlay.GLText.GLT_BOTTOM;
import static com.unlikepaladin.pfm.client.screens.overlay.GLText.GLT_CENTER;

@Mixin(LoadingOverlay.class)
public class PFMLoadingOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    private GLText pfm$glText;
    @Unique
    private GLText.GLTtext pfm$assemblingFurniture;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadInstance;getActualProgress()F"))
    private void onRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!PFMRuntimeResources.isAnyGeneratorRunning()) {
            if (this.pfm$assemblingFurniture != null) {
                GLText.gltDeleteText(this.pfm$assemblingFurniture);
                this.pfm$assemblingFurniture = null;
            }
            return;
        }

        if (!BlockItemRegistry.isModLoaded("vulkanmod")) {
            if (pfm$glText == null || this.pfm$assemblingFurniture == null) {
                this.pfm$glText = GLText.shared();
                this.pfm$assemblingFurniture = GLText.gltCreateText();
            }
            pfm$glText.gltViewport(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());

            try (Closeable ignored1 = pfm$glText.gltBeginDraw()) {
                float textScale = (float) (minecraft.getWindow().getGuiScale() / 2.0f) * 1.5f;
                pfm$glText.gltColor(1.0f, 1.0f, 1.0f, 0.01f);
                GLText.gltSetText(pfm$assemblingFurniture, "Assembling Paladin's Furniture!");
                pfm$glText.gltDrawText2DAligned(
                        this.pfm$assemblingFurniture,
                        this.minecraft.getWindow().getWidth() / 2.0f,
                        this.minecraft.getWindow().getHeight() - (GLText.gltGetTextHeight(pfm$assemblingFurniture, textScale) + GLText.gltGetTextHeight(pfm$assemblingFurniture, textScale)) + 10f,
                        textScale,
                        GLT_CENTER, GLT_BOTTOM
                );
            } catch (Exception ignored) {

            }
        }
    }
}
