package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.screens.overlay.GLText;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Closeable;
import java.util.Objects;

import static com.unlikepaladin.pfm.client.screens.overlay.GLText.GLT_BOTTOM;
import static com.unlikepaladin.pfm.client.screens.overlay.GLText.GLT_CENTER;

@Mixin(SplashOverlay.class)
public class PFMSplashOverlayMixin {
    @Shadow @Final private MinecraftClient client;
    private final GLText glText;
    private final GLText.GLTtext assemblingFurniture;

    public PFMSplashOverlayMixin() {
        this.glText = new GLText();
        this.assemblingFurniture = GLText.gltCreateText();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceReload;getProgress()F"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PFMRuntimeResources.isAnyGeneratorRunning()) {
            glText.gltViewport(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());

            try (Closeable ignored1 = glText.gltBeginDraw()) {
                float textScale = (float) (client.getWindow().getScaleFactor() / 2.0f) * 1.5f;
                glText.gltColor(1.0f, 1.0f, 1.0f, 0.01f);
                GLText.gltSetText(assemblingFurniture, "Assembling Paladin's Furniture!");
                glText.gltDrawText2DAligned(
                        this.assemblingFurniture,
                        this.client.getWindow().getFramebufferWidth() / 2.0f,
                        this.client.getWindow().getFramebufferHeight() - (GLText.gltGetTextHeight(assemblingFurniture, textScale) + GLText.gltGetTextHeight(assemblingFurniture, textScale)) + 10f,
                        textScale,
                        GLT_CENTER, GLT_BOTTOM
                );
            } catch (Exception ignored) {

            }
        }
    }
}
