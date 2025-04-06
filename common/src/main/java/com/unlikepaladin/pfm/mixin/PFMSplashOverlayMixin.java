package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.screens.overlay.GLText;
import com.unlikepaladin.pfm.registry.BlockItemRegistry;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
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

@Mixin(SplashOverlay.class)
public class PFMSplashOverlayMixin {
    @Shadow @Final private MinecraftClient client;
    @Unique
    private GLText pfm$glText;
    @Unique
    private GLText.GLTtext pfm$assemblingFurniture;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourceReload;getProgress()F"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PFMRuntimeResources.isAnyGeneratorRunning()) {
            if (!BlockItemRegistry.isModLoaded("vulkanmod")) {
                if (pfm$glText == null || this.pfm$assemblingFurniture == null) {
                    this.pfm$glText = new GLText();
                    this.pfm$assemblingFurniture = GLText.gltCreateText();
                }
                pfm$glText.gltViewport(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());

                try (Closeable ignored1 = pfm$glText.gltBeginDraw()) {
                    float textScale = (float) (client.getWindow().getScaleFactor() / 2.0f) * 1.5f;
                    pfm$glText.gltColor(1.0f, 1.0f, 1.0f, 0.01f);
                    GLText.gltSetText(pfm$assemblingFurniture, "Assembling Paladin's Furniture!");
                    pfm$glText.gltDrawText2DAligned(
                            this.pfm$assemblingFurniture,
                            this.client.getWindow().getFramebufferWidth() / 2.0f,
                            this.client.getWindow().getFramebufferHeight() - (GLText.gltGetTextHeight(pfm$assemblingFurniture, textScale) + GLText.gltGetTextHeight(pfm$assemblingFurniture, textScale)) + 10f,
                            textScale,
                            GLT_CENTER, GLT_BOTTOM
                    );
                } catch (Exception ignored) {

                }
            }
        }
    }
}
