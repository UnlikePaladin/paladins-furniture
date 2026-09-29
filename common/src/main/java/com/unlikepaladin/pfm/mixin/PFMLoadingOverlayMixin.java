package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import com.unlikepaladin.pfm.client.screens.overlay.PFMOverlayTextRenderer;
import com.unlikepaladin.pfm.runtime.PFMRuntimeResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class PFMLoadingOverlayMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "registerTextures", at = @At("HEAD"))
    private static void onRegisterTextures(TextureManager textureManager, CallbackInfo ci) {
        PFMGeneratingOverlay.registerTextures(textureManager);
    }

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadInstance;getActualProgress()F"))
    private void onRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!PFMRuntimeResources.isAnyGeneratorRunning()) {
            return;
        }

        PFMOverlayTextRenderer.register(this.minecraft.getTextureManager());

        int width = context.guiWidth();
        int height = context.guiHeight();
        float barY = (float) (height * 0.8325);

        PFMOverlayTextRenderer.drawCenteredString(context, "Assembling Paladin's Furniture!", width / 2.0f, barY + 10, 0.75f, 0xFFFFFFFF);
    }
}
