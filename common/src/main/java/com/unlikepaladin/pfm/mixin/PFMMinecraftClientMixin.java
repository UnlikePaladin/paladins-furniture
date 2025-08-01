package com.unlikepaladin.pfm.mixin;

import com.unlikepaladin.pfm.client.PFMClientExtension;
import com.unlikepaladin.pfm.client.screens.overlay.PFMGeneratingOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class PFMMinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements PFMClientExtension {
    @Shadow @Final private TextureManager textureManager;

    protected PFMMinecraftClientMixin() {
        super("Client");
    }

    @Override
    public void invoke$runTasks() {
        this.runTasks();
    }
}
