package com.unlikepaladin.pfm.mixin.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;
import java.net.URISyntaxException;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    private static boolean pfm$firstInit;

    @Inject(method = "init", at = @At("RETURN"))
    public void pfm$showMissingDependencyScreen(CallbackInfo ci) {
        if (pfm$firstInit) {
            return;
        }

        pfm$firstInit = true;

        String reason;

        if (FabricLoader.getInstance().isModLoaded("sandwichable") && !FabricLoader.getInstance().isModLoaded("advanced_runtime_resource_pack")) {
            reason = "pfm.compat.failure.reason.arrpNotFound";
        }
        else {
            return;
        }

        MinecraftClient.getInstance().setScreen(new ConfirmScreen(
                (boolean accepted) -> {
                    if (accepted) {
                        try {
                            Util.getOperatingSystem().open(new URI("https://www.curseforge.com/minecraft/mc-mods/arrp/files/3529149"));
                        } catch (URISyntaxException e) {
                            throw new IllegalStateException(e);
                        }
                    } else {
                        MinecraftClient.getInstance().stop();
                    }
                },
                Text.translatable("pfm.compat.failure.title").formatted(Formatting.RED),
                Text.translatable(reason),
                Text.translatable("pfm.compat.failure.arrpDownload"),
                Text.translatable("menu.quit")));
    }
}
