package com.unlikepaladin.pfm.mixin;

import com.google.common.collect.ImmutableList;
import com.unlikepaladin.pfm.PaladinFurnitureMod;
import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PopupScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class PFMTitleScreenMixin extends Screen {
    @Unique
    private static boolean pfm$firstInit;

    protected PFMTitleScreenMixin(Component title) {
        super(title);
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
            url = "https://modrinth.com/mod/indium/versions?g=" + SharedConstants.VERSION_STRING;
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
                    new TranslatableComponent("pfm.compat.failure.title").withStyle(ChatFormatting.RED),
                    new TranslatableComponent(reason),
                    new TranslatableComponent("pfm.compat.failure.indiumDownload"),
                    new TranslatableComponent("menu.quit")));
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
                    new TranslatableComponent("pfm.compat.issue.title").withStyle(ChatFormatting.YELLOW),
                    new TranslatableComponent(reason),
                    new TranslatableComponent("pfm.compat.issue.connectorReport"),
                    new TranslatableComponent("options.graphics.warning.accept")));

        }
    }
}