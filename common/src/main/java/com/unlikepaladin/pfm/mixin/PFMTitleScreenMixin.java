package com.unlikepaladin.pfm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class PFMTitleScreenMixin {

    @Inject(method = "registerTextures", at = @At(value = "HEAD"))
    private static void initializeAdditionalPFMTextures(CallbackInfo ci, @Local(argsOnly = true) TextureManager textureManager) {
        PFMGeneratingOverlay.registerTextures(textureManager);
    }

}
