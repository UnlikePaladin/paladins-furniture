package com.unlikepaladin.pfm.mixin.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
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
public class PFMTitleScreenMixin {
    @Unique
    private static boolean pfm$firstInit;

    @Inject(method = "init", at = @At("RETURN"))
    public void pfm$showMissingDependencyScreen(CallbackInfo ci) {
        if (pfm$firstInit) {
            return;
        }

        pfm$firstInit = true;

        String reason;

        if (FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("indium")) {
            reason = "pfm.compat.failure.reason.indiumNotFound";
        }
        else {
            return;
        }

        MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                (boolean accepted) -> {
                    if (accepted) {
                        try {
                            Util.getOperatingSystem().open(new URI("https://modrinth.com/mod/indium/versions?g=1.17.1"));
                        } catch (URISyntaxException e) {
                            throw new IllegalStateException(e);
                        }
                    } else {
                        MinecraftClient.getInstance().stop();
                    }
                },
                Text.translatable("pfm.compat.failure.title").formatted(Formatting.RED),
                Text.translatable(reason),
                Text.translatable("pfm.compat.failure.indiumNotFound"),
                Text.translatable("menu.quit")));
    }
}
