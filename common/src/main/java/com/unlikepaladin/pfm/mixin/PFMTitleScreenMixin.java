package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import com.llamalad7.mixinextras.sugar.Local;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;
import java.net.URISyntaxException;

@Mixin(TitleScreen.class)
public abstract class PFMTitleScreenMixin extends Screen {
    @Unique
    private static boolean pfm$firstInit;

    protected PFMTitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "registerTextures", at = @At(value = "HEAD"))
    private static void initializeAdditionalPFMTextures(CallbackInfo ci, @Local(argsOnly = true) TextureManager textureManager) {
        PFMGeneratingOverlay.registerTextures(textureManager);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void pfm$showMissingDependencyScreen(CallbackInfo ci) {
        if (pfm$firstInit) {
            return;
        }

        pfm$firstInit = true;

        String reason;
        String url;

        if (PFMFileUtil.getModLoader() == PFMFileUtil.ModLoader.FABRIC && PFMFileUtil.isModLoaded("sodium") && !PFMFileUtil.isModLoaded("indium")) {
            reason = "pfm.compat.failure.reason.indiumNotFound";
            url = "https://modrinth.com/mod/indium/versions?g=" + SharedConstants.getCurrentVersion().name();
            if (!Version.compareVersions(PFMFileUtil.getVersion("sodium").get(), "0.6")) {

            Minecraft.getInstance().setScreen(new ConfirmScreen(
                    (boolean accepted) -> {
                        if (accepted) {
                            try {
                                Util.getPlatform().openUri(new URI(url));
                            } catch (URISyntaxException e) {
                                throw new IllegalStateException(e);
                            }
                        } else {
                            Minecraft.getInstance().stop();
                        }
                    },
                    Component.translatable("pfm.compat.failure.title").withStyle(ChatFormatting.RED),
                    Component.translatable(reason),
                    Component.translatable("pfm.compat.failure.indiumDownload"),
                    Component.translatable("menu.quit")));
            }
        } else if (PFMFileUtil.isModLoaded("connectormod")&& !PaladinFurnitureMod.getPFMConfig().disableSinytraWarning()) {
            reason = "pfm.compat.issue.reason.connectorMod";

            Minecraft.getInstance().setScreen(new ConfirmScreen(
                    (boolean accepted) -> {
                        if (accepted) {
                            try {
                                Util.getPlatform().openUri(new URI("https://github.com/Sinytra/ForgifiedFabricAPI/issues/186"));
                            } catch (URISyntaxException e) {
                                throw new IllegalStateException(e);
                            }
                        } else {
                            Minecraft.getInstance().setScreen(this);
                        }
                    },
                    Component.translatable("pfm.compat.issue.title").withStyle(ChatFormatting.YELLOW),
                    Component.translatable(reason),
                    Component.translatable("pfm.compat.issue.connectorReport"),
                    Component.translatable("options.graphics.warning.accept")));

        }
    }
}