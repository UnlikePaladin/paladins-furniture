package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.utilities.PFMFileUtil;
import com.unlikepaladin.pfm.utilities.Version;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
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

    protected PFMTitleScreenMixin(Text title) {
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
            url = "https://modrinth.com/mod/indium/versions?g=" + SharedConstants.VERSION_NAME;
            if (!Version.compareVersions(PFMFileUtil.getVersion("sodium").get(), "0.6")) {

            MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                    (boolean accepted) -> {
                        if (accepted) {
                            try {
                                Util.getOperatingSystem().open(new URI(url));
                            } catch (URISyntaxException e) {
                                throw new IllegalStateException(e);
                            }
                        } else {
                            MinecraftClient.getInstance().stop();
                        }
                    },
                    new TranslatableText("pfm.compat.failure.title").formatted(Formatting.RED),
                    new TranslatableText(reason),
                    new TranslatableText("pfm.compat.failure.indiumDownload"),
                    new TranslatableText("menu.quit")));
            }
        } else if (PFMFileUtil.isModLoaded("connectormod")) {
            reason = "pfm.compat.issue.reason.connectorMod";

            MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                    (boolean accepted) -> {
                        if (accepted) {
                            try {
                                Util.getOperatingSystem().open(new URI("https://github.com/Sinytra/ForgifiedFabricAPI/issues/186"));
                            } catch (URISyntaxException e) {
                                throw new IllegalStateException(e);
                            }
                        } else {
                            MinecraftClient.getInstance().setScreen(this);
                        }
                    },
                    new TranslatableText("pfm.compat.issue.title").formatted(Formatting.YELLOW),
                    new TranslatableText(reason),
                    new TranslatableText("pfm.compat.issue.connectorReport"),
                    new TranslatableText("options.graphics.warning.accept")));

        }
    }
}
